package app;

import client.UserConsole;
import commands.CommandManager;
import commands.CommandNameToInfo;
import commands.pcommands.*;
import communication.*;
import exceptions.ReconnectionTimoutException;
import exceptions.ResponseCodeException;
import model.CollectionFilter;
import utils.Console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ClientMain {
  public static void main(String[] args) {
    Session clientSession = new ClientSession("localhost", 2222);
    if (!waitForConnection(clientSession)) {
      System.out.println("Connection failed. Timeout reached.");
      return;
    }

    Transceiver clientTransceiver = new ClientTransceiver(clientSession.getSocketChannel());
    Exchanger exchanger = new ClientExchanger(clientTransceiver, clientSession);

    CollectionFilter collectionFilter = new CollectionFilter();
    try {
      exchanger.requestCollectionUpdate();
      collectionFilter.loadCollection(exchanger.recieveCollectionWrapper());
    } catch (ReconnectionTimoutException | ResponseCodeException e) {
      System.err.println("Failed to load collection. Exiting with error: " + e.getMessage());
      return;
    }

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

    CommandNameToInfo commandNameToInfo;
    try {
      commandNameToInfo = getaccessibleCommandsInfo(exchanger);
    } catch (ReconnectionTimoutException | ResponseCodeException e) {
      System.out.println("Can't get accessible commands: " + e.getMessage());
      return;
    }
    clientCommandManager.addCommandInfos(commandNameToInfo);

    userConsole.run();
  }

  private static boolean waitForConnection(Session clientSession) {
    return clientSession.reconnect(15);
  }

  private static CommandNameToInfo getaccessibleCommandsInfo(Exchanger exchanger) throws ReconnectionTimoutException, ResponseCodeException {
    exchanger.requestAccessibleCommandsInfo();
    return exchanger.recieveAccessibleCommandsInfo();
  }
}