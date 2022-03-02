package app;

import commands.CommandManager;
import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import communication.RequestPurpose;
import communication.ResponseCode;
import communication.packaging.BaseResponse;
import communication.packaging.Message;
import communication.packaging.Request;
import communication.packaging.Response;
import lombok.extern.log4j.Log4j2;
import model.CollectionWrapper;
import server.collection.CollectionManager;
import server.communication.MessagesProcessor;
import server.communication.ServerTransceiver;
import utils.Exitable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class Server implements Exitable {
  volatile boolean runningFlag;

  private final int port;
  private final CommandManager commandManager;
  private final CollectionManager collectionManager;
  private final BufferedReader bufferedReader;

  private final Selector selector;
  private ServerSocketChannel serverChannel;

  private SocketChannel readableChannel;

  private final Map<SocketChannel, Request> socketToRequest = new ConcurrentHashMap<>();

  public Server(int port, CommandManager commandManager, CollectionManager collectionManager) throws IOException {
    this.port = port;
    this.commandManager = commandManager;
    this.collectionManager = collectionManager;
    this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    selector = initSelector();
  }

  private Selector initSelector() throws IOException {
    Selector socketSelector = SelectorProvider.provider().openSelector();

    serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);
    serverChannel.socket().bind(new InetSocketAddress(port));
    serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

    return socketSelector;
  }

  public void run() {
    runningFlag = true;
    log.info("Server started");

    while (runningFlag) {
      try {
        selector.select(200);
      } catch (IOException ignored) {
      }
      handleClients();
      handleServerInput();
    }

    log.info("Server finished");
  }

  private void handleClients() {
    Set<SelectionKey> keys = selector.selectedKeys();
    for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext(); ) {
      SelectionKey key = it.next();
      it.remove();
      handleSelectedKey(key);
    }
  }

  private void handleSelectedKey(SelectionKey key) {
      if (!key.isValid()) return;
      try {
        if (key.isConnectable()) {
          log.debug("New connectable channel");
          onChannelConnectable(key);
        } else if (key.isAcceptable()) {
          log.debug("New acceptable channel");
          onChannelAcceptable();
        } else if (key.isReadable()) {
          log.debug("New readable channel");
          onChannelReadable(key);
        } else if (key.isWritable()) {
          log.debug("New writable channel");
          onChannelWritable(key);
        }
      } catch (CancelledKeyException e) {
        log.info("Canceled key: {}", e.getMessage());
      } catch (IOException e) {
        log.info(e.getMessage());
      }
  }

  private void onChannelConnectable(SelectionKey key) {
    log.debug("New connectable channel: {}", key.channel());
  }

  private void onChannelAcceptable() throws IOException {
    SocketChannel socketChannel = serverChannel.accept();
    socketChannel.configureBlocking(false);
    socketChannel.register(selector, SelectionKey.OP_READ);
  }

  private void onChannelReadable(SelectionKey key) {
    this.readableChannel = (SocketChannel) key.channel();
    readRequest().ifPresentOrElse(this::registerRequest, key::cancel);
  }

  private Optional<Request> readRequest() {
    try {
      return Optional.of((Request) ServerTransceiver.of(readableChannel).recieve());
    } catch (IOException e) {
      log.warn("Client disconnected");
      closeChannel(readableChannel);
    } catch(ClassNotFoundException e) {
      log.warn("Error while reading Request: {}", e.getMessage());
    }
    return Optional.empty();
  }

  private void closeChannel(SocketChannel socketChannel) {
    try {
      socketChannel.close();
    } catch (IOException ignore) {}
  }

  private void registerRequest(Request newRequest) {
    socketToRequest.put(readableChannel, newRequest);
    try {
      readableChannel.register(selector, SelectionKey.OP_WRITE);
    } catch (ClosedChannelException e) {
      log.info("Socket channel closed");
    }
  }

  private void onChannelWritable(SelectionKey key) throws IOException {
    SocketChannel writableChannel = (SocketChannel) key.channel();
    Request clientRequest = socketToRequest.get(writableChannel);
    MessagesProcessor msgProccessor = MessagesProcessor.of(clientRequest);

    if (!msgProccessor.isPayloadValid()) {
      log.warn("Message payload is invalid");
    } else {
      msgProccessor.setResponsePayload(proccessRequest(clientRequest));
    }

    Response response = msgProccessor.buildResponse();

    ServerTransceiver.of(writableChannel).send(response);
    writableChannel.register(selector, SelectionKey.OP_READ);

    if (clientRequest.getPurpose() == RequestPurpose.EXECUTE) {
      writeUpdatedCollectionToAll();
    }
  }

  private void writeUpdatedCollectionToAll() {
    Set<SelectionKey> keys = selector.keys();
    log.debug("Selected keys: {}", keys);
    CollectionWrapper wrapper = collectionManager.getWrapper();
    for (SelectionKey key : keys) {
      writeUpdatedCollection(key, wrapper);
    }
  }

  private void writeUpdatedCollection(SelectionKey key, CollectionWrapper wrapper) {
    log.debug("Keys: {}", selector.keys());
    log.debug("Sending update to {}", key.channel());
    if (key.channel() instanceof ServerSocketChannel) return;
    SocketChannel socketChannel = (SocketChannel) key.channel();
    Message collectionMessage = BaseResponse.of(RequestPurpose.UPDATE_COLLECTION, ResponseCode.SUCCESS, wrapper);
    SocketAddress remoteAddr = null;
    try {
      remoteAddr = socketChannel.getRemoteAddress();
      ServerTransceiver.of(socketChannel).send(collectionMessage);
    } catch (IOException e) {
      log.info("Failed to sent updated collection to client {} '{}'", remoteAddr, e.getMessage());
    }
  }

  private Object proccessRequest(Request request) {
    return switch (request.getPurpose()) {
      case EXECUTE -> executeCommand((ExecutionPayload) request.getPayload().get());
      case GET_COMMANDS -> getAccessibleCommandsInfo();
      case UPDATE_COLLECTION -> collectionManager.getWrapper();
    };
  }

  private ExecutionResult executeCommand(ExecutionPayload payload) {
    return commandManager.executeCommand(payload);
  }

  private CommandNameToInfo getAccessibleCommandsInfo() {
    log.debug("Requested accessible commands");
    return commandManager.getUseraccessibleCommandsInfo();
  }

  private void handleServerInput() {
    String userInput;
    try {
      if (System.in.available() == 0) return;

      userInput = bufferedReader.readLine();
    } catch (IOException ignore) {return;}

    if (null == userInput || userInput.trim().equals("exit")) {
      log.warn("Stopping server on 'exit'");
      exit();
    }
  }

  private void stop() {
    selector.wakeup();
    try {
      selector.close();
      serverChannel.socket().close();
      serverChannel.close();
    } catch (IOException e) {
      log.info("Can't close socket");
    }
  }

  @Override
  public void exit() {
    runningFlag = false;
    stop();
  }
}
