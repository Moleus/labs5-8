package app;

import commands.CommandManager;
import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import communication.packaging.Request;
import communication.packaging.Response;
import exceptions.RecievedInvalidObjectException;
import lombok.extern.log4j.Log4j2;
import model.data.Flat;
import server.authorization.UserManager;
import server.collection.CollectionManager;
import server.communication.MessagesProcessor;
import server.communication.ServerTransceiver;
import utils.Exitable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
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
  private final CollectionManager<Flat> collectionManager;
  private final UserManager userManager;
  private final BufferedReader bufferedReader;

  private final Selector selector;
  private ServerSocketChannel serverChannel;

  private SocketChannel readableChannel;

  private final Map<SocketChannel, Request> socketToRequest = new ConcurrentHashMap<>();

  public Server(
      int port,
      CommandManager commandManager,
      CollectionManager<Flat> collectionManager,
      UserManager userManager) throws IOException {
    this.port = port;
    this.commandManager = commandManager;
    this.collectionManager = collectionManager;
    this.userManager = userManager;
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
        if (key.isAcceptable()) {
          onChannelAcceptable();
        } else if (key.isReadable()) {
          onChannelReadable(key);
        } else if (key.isWritable()) {
          onChannelWritable(key);
        }
      } catch (CancelledKeyException e) {
        log.info("Canceled key: {}", e.getMessage());
      } catch (IOException e) {
        log.info(e.getMessage());
      }
  }

  private void onChannelAcceptable() throws IOException {
    log.debug("New client connected");
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
      return ServerTransceiver.of(readableChannel).recieve().map(Request.class::cast);
    } catch (IOException e) {
      log.warn("Client disconnected");
      closeChannel(readableChannel);
    } catch (RecievedInvalidObjectException e) {
      log.error("Recieved invalid Request: {}", e.getMessage());
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
      log.warn("Payload for message {} is invalid: {}", clientRequest.getPurpose(), clientRequest.getPayload());
    } else {
      Object responsePayload = proccessRequest(clientRequest);
      log.debug(responsePayload);
      msgProccessor.setResponsePayload(responsePayload);
    }

    Response response = msgProccessor.buildResponse();

    ServerTransceiver.of(writableChannel).send(response);
    writableChannel.register(selector, SelectionKey.OP_READ);
  }

  private Object proccessRequest(Request request) {
    return switch (request.getPurpose()) {
      case EXECUTE -> executeCommand((ExecutionPayload) request.getPayload().get());
      case GET_COMMANDS -> getAccessibleCommandsInfo();
      case INIT_COLLECTION -> collectionManager.getFullCollection();
      case UPDATE_COLLECTION -> collectionManager.getChangesNewerThan((Long) request.getPayload().get());
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
