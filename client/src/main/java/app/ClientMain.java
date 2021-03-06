package app;

import client.Console;
import client.UserConsole;
import collection.CollectionFilter;
import commands.CommandManager;
import commands.CommandNameToInfo;
import commands.pcommands.*;
import communication.*;
import exceptions.InvalidCredentialsException;
import exceptions.ResponseCodeException;
import model.data.Flat;

import java.io.IOException;
import java.io.PrintStream;

public class ClientMain {
  public static void main(String[] args) {
    Session clientSession = new ClientSession("localhost", 2222);
    if (!waitForConnection(clientSession)) {
      System.out.println("Connection failed. Timeout reached.");
      return;
    }

    Transceiver clientTransceiver = new ClientTransceiver(clientSession.getSocketChannel());
    Exchanger<Flat> exchanger = new ClientExchanger<>(clientTransceiver, clientSession);

    CollectionFilter collectionFilter;
    try {
      exchanger.requestFullCollection();
      collectionFilter = new CollectionFilter(exchanger.receiveFullCollection());
    } catch (IOException | InvalidCredentialsException e) {
      System.err.println("Failed to load collection. Exiting with error: " + e.getMessage());
      return;
    }

    PrintStream writer = new PrintStream(System.out);
    CommandManager clientCommandManager = new CommandManager();
    Console userConsole = new UserConsole(writer, clientCommandManager, exchanger, collectionFilter);
    Authenticator authenticator = new ClientAuthenticator(exchanger);

    clientCommandManager.registerCommands(
        new Help(clientCommandManager),
        new Exit(userConsole),
        new ExecuteScript(userConsole),
        new Info(collectionFilter),
        new FilterContainsName(collectionFilter),
        new PrintUniqueNumberOfRooms(collectionFilter),
        new PrintFieldDescendingNew(collectionFilter),
        new Login(authenticator),
        new Register(authenticator)
    );

    CommandNameToInfo commandNameToInfo;
    try {
      commandNameToInfo = getAccessibleCommandsInfo(exchanger);
    } catch (ResponseCodeException | IOException | InvalidCredentialsException e) {
      System.out.println("Can't get accessible commands: " + e.getMessage());
      return;
    }
    clientCommandManager.addCommandInfos(commandNameToInfo);

    userConsole.run();
  }

  private static boolean waitForConnection(Session clientSession) {
    return clientSession.reconnect(15);
  }

  private static CommandNameToInfo getAccessibleCommandsInfo(Exchanger<Flat> exchanger) throws ResponseCodeException, IOException, InvalidCredentialsException {
    exchanger.requestAccessibleCommandsInfo();
    return exchanger.receiveAccessibleCommandsInfo();
  }
}