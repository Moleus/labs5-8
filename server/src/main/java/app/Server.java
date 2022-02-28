package app;

import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import communication.packaging.Request;
import communication.packaging.Response;
import lombok.extern.log4j.Log4j2;
import server.commands.CommandManager;
import server.communication.MessagesProccessor;
import server.communication.ServerTransceiver;
import utils.Exitable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class Server implements Exitable {
  volatile boolean runningFlag;
  private final CommandManager commandManager;
  private final ServerSocketChannel serverChannel;
  private final BufferedReader bufferedReader;
  private final Selector selector;

  private SocketChannel readableChannel;

  private static final Map<SocketChannel, Request> socketToRequest = new ConcurrentHashMap<>();

  public Server(int port, CommandManager commandManager) throws IOException {
    this.commandManager = commandManager;
    this.serverChannel = ServerSocketChannel.open();
    this.serverChannel.socket().bind(new InetSocketAddress(port));
    this.serverChannel.configureBlocking(false);
    this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    this.selector = Selector.open();
  }

  public void run() {
    try {
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    } catch (IOException ignored){}

    runningFlag = true;
    log.info("Server started");

    Thread exitHandler = new Thread(() -> {
      while (runningFlag) {
        handleServerInput();
        Thread.onSpinWait();
      }
    });
    exitHandler.start();

    while (runningFlag) {
      try {
        selector.selectNow();
      } catch (IOException ignored) {}
      handleClients();
    }

    exitHandler.interrupt();
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
          log.debug("New acceptable channel");
          onChannelAcceptable();
        } else if (key.isReadable()) {
          log.debug("New readable channel");
          onChannelReadable(key);
        } else if (key.isWritable()) {
          log.debug("New writable channel");
          onChanngelWritable(key);
        }
      } catch (CancelledKeyException e) {
        log.info("Canceled key: {}", e.getMessage());
      } catch (IOException e) {
        log.info(e.getMessage());
      }
    }

  private void onChannelAcceptable() throws IOException {
    SocketChannel socketChannel = serverChannel.accept();
    socketChannel.configureBlocking(false);
    socketChannel.register(selector, SelectionKey.OP_READ);
  }

  private void onChannelReadable(SelectionKey key) {
    this.readableChannel = (SocketChannel) key.channel();
    readRequest().ifPresent(this::registerRequest);
  }

  private Optional<Request> readRequest() {
    try {
      return Optional.of((Request) ServerTransceiver.of(readableChannel).recieve());
    } catch (IOException e) {
      log.warn("Failed to recieve request object");
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

  private void onChanngelWritable(SelectionKey key) throws IOException {
    SocketChannel writableChannel = (SocketChannel) key.channel();
    Request clientRequest = socketToRequest.get(writableChannel);
    MessagesProccessor msgProccessor = MessagesProccessor.of(clientRequest);

    if (!msgProccessor.isPayloadValid()) {
      log.warn("Message payload is invalid");
    } else {
      msgProccessor.setResponsePayload(proccessRequest(clientRequest));
    }

    Response response = msgProccessor.buildResponse();
    ServerTransceiver.of(writableChannel).send(response);
    writableChannel.register(selector, SelectionKey.OP_READ);
  }

  private Object proccessRequest(Request request) {
    return switch (request.getPurpose()) {
      case EXECUTE -> executeCommand((ExecutionPayload) request.getPayload().get());
      case GET_COMMANDS -> getAccessibleCommandsInfo();
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
