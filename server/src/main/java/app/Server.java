package app;

import commands.CommandManager;
import interfaces.Exitable;
import lombok.extern.log4j.Log4j2;
import model.data.Flat;
import server.authentication.UserManager;
import server.collection.CollectionManager;
import server.communication.NioReactor;
import server.communication.ThreadPoolDispatcher;
import server.handlers.ChannelWrapper;
import server.handlers.RequestHandler;
import server.handlers.ServerChannelWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class Server implements Exitable {
  volatile boolean runningFlag;

  private final int port;
  private final CommandManager commandManager;
  private final CollectionManager<Flat> collectionManager;
  private final UserManager userManager;
  private final BufferedReader bufferedReader;

  private final NioReactor reactor;

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

    this.reactor = new NioReactor(new ThreadPoolDispatcher(10));
    ChannelWrapper channelWrapper = initChannel();
    reactor.registerChannel(channelWrapper);
  }

  private ChannelWrapper initChannel() throws IOException {
    RequestHandler handler = new RequestHandler(collectionManager, userManager, commandManager);
    ServerChannelWrapper channel = new ServerChannelWrapper(handler, port);
    channel.bind();
    return channel;
  }

  public void run() throws IOException {
    runningFlag = true;
    log.info("Server started");

    reactor.start();
    handleServerInput();
  }

  private void handleServerInput() {
    while (true) {
      try {
        String userInput = bufferedReader.readLine();
        if (null == userInput || userInput.trim().equals("exit")) {
          log.warn("Stopping server on 'exit'");
          exit();
        }
      } catch (IOException ignore) {
        return;
      }
    }
  }

  private void stop() {
    try {
      reactor.stop();
    } catch (InterruptedException | IOException e) {
      System.out.println("Server stopped");
    }
  }

  @Override
  public void exit() {
    runningFlag = false;
    stop();
  }
}
