package app;

import client.*;
import commands.Command;
import commands.CommandNameToInfo;
import commands.pcommands.ExecuteScript;
import commands.pcommands.Exit;
import communication.*;
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

    Optional<CommandNameToInfo> commandsOp = getaccessibleCommandsInfo(exchanger);
    if (commandsOp.isEmpty()) {
      return;
    }

    CommandNameToInfo commandNameToInfo = commandsOp.get();

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    PrintStream writer = new PrintStream(System.out);
    Console userConsole = new UserConsole(reader, writer, commandNameToInfo, exchanger);

    Map<String, Command> clientSpecificCommands = new HashMap<>();
    clientSpecificCommands.put("exit", new Exit(userConsole));
    clientSpecificCommands.put("execute_script", new ExecuteScript(userConsole));
    userConsole.registerLocalCommnands(clientSpecificCommands);

    userConsole.run();
  }

  private static boolean waitForConnection(Session clientSession) {
    return clientSession.reconnect();
  }

  private static Optional<CommandNameToInfo> getaccessibleCommandsInfo(Exchanger exchanger) {
    int tries = 0;
    int maxTries = 120;
    while (tries++ < maxTries) {
      try {
        exchanger.requestAccessibleCommandsInfo();
        Thread.sleep(50);
        Optional<CommandNameToInfo> commansOp = exchanger.readaccessibleCommandsInfoResponse();
        if (commansOp.isPresent()) return commansOp;
        System.out.println("Waiting for connection");
        Thread.sleep(1000);
      } catch (InterruptedException ignore) {}
        catch (IOException e) {
        System.out.println("Failed to request accessible commands from server. Exiting");
        break;
      }
    }
    return Optional.empty();
  }
}