package app;

import client.UserConsole;
import commands.CommandManager;
import commands.CommandNameToInfo;
import commands.pcommands.*;
import communication.*;
import exceptions.ReconnectionTimoutException;
import model.CollectionFilter;
import utils.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientMain {
  public static void main(String[] args) {
    Session clientSession = new ClientSession("localhost", 2222);
    if (!waitForConnection(clientSession)) {
      System.out.println("Connection failed. Timeout reached.");
      return;
    }
    System.out.println("Connected to server");

    Transceiver clientTransceiver = new ClientTransceiver(clientSession.getSocketChannel());
    ClientExchanger exchanger = new ClientExchanger(clientTransceiver, clientSession);

    CollectionFilter collectionFilter = new CollectionFilter();
    try {
      exchanger.requestCollectionUpdate();
      collectionFilter.loadCollection(exchanger.fetchUpdatedCollection(true));
    } catch (IOException | ClassNotFoundException | ReconnectionTimoutException e) {
      System.err.println("Failed to load collection. Exiting with error: " + e.getMessage());
      return;
    }

    Optional<CommandNameToInfo> commandsOp = getaccessibleCommandsInfo(exchanger);
    if (commandsOp.isEmpty()) {
      System.out.println("Can't get accessible commands stopping.");
      return;
    }

    CommandNameToInfo commandNameToInfo = commandsOp.get();

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    PrintStream writer = new PrintStream(System.out);
    CommandManager clientCommandManager = new CommandManager();
    Console userConsole = new UserConsole(reader, writer, clientCommandManager, exchanger, collectionFilter);

    clientCommandManager.registerCommands(
        new Help(clientCommandManager),
        new Exit(userConsole),
        new ExecuteScript(userConsole),
        new Info(collectionFilter),
        new FilterContainsName(collectionFilter),
        new PrintUniqueNumberOfRooms(collectionFilter),
        new PrintFieldDescendingNew(collectionFilter)
    );

    CommandNameToInfo commandNameToInfo = commandsOp.get();
    clientCommandManager.addCommandInfos(commandNameToInfo);

    userConsole.run();
  }

  private static boolean waitForConnection(Session clientSession) {
    return clientSession.reconnect(15);
  }

  private static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {
    }
  }

  private static Optional<CommandNameToInfo> getaccessibleCommandsInfo(Exchanger exchanger) {
    int tries = 0;
    int maxTries = 120;
    while (tries++ < maxTries) {
      try {
        exchanger.requestAccessibleCommandsInfo();
        sleep(250);
        Optional<CommandNameToInfo> commansOp = exchanger.readaccessibleCommandsInfoResponse();
        if (commansOp.isPresent()) return commansOp;
      } catch (IOException | ReconnectionTimoutException e) {
        System.out.println("Failed to request accessible commands from server. Exiting");
        break;
      }
    }
    return Optional.empty();
  }
}