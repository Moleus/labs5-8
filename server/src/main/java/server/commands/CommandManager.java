package server.commands;

import commands.Command;
import commands.CommandNameToInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Manager class which stores all commands. Provieds an API to register new commands and execute them.
 */
public class CommandManager {
  private final Map<String, Command> strToCommand;
  
  public CommandManager() {
    this.strToCommand = new HashMap<>();
  }

  /**
   * Registers passed commands in this manager.
   * @param commands commands to register
   */
  public void registerCommands(Command ... commands) {
    // add commands to HashMap
    for (Command cmd : commands) {
      registerCommand(cmd);
    }
  }

  /**
   * Checks if command is registered in command manager.
   */
  private boolean isInCommands(String strCommand) {
    return strToCommand.get(strCommand) != null;
  }
  
  private void registerCommand(Command cmd) {
    Objects.requireNonNull(cmd);
    String commandName = cmd.getName();
    if (isInCommands(commandName)) { // found a duplicate
      System.err.println("Command already registered");
      return;
    }
    strToCommand.put(commandName, cmd);
  }

  /**
   * Executes command specified in request.
   * @param payload data-object with parameters required for command execution.
   * @return {@link ExecutionResult} data-object with execution result.
   */
  public ExecutionResult executeCommand(ExecutionPayload payload) {
    String commandName = payload.getCommandName();
    if (!isRegistered(commandName)) {
      return ExecutionResult.valueOf(false, "Invalid command");
    }
    return strToCommand.get(commandName).execute(payload);
  }

  /**
   * Return true if command is registered in manager, otherwise - false.
   */
  public boolean isRegistered(String commandName) {
    return strToCommand.containsKey(commandName);
  }

  /**
   * Returns List of all registered commands names.
   */
  public List<String> getCommandNames() {
    return strToCommand.keySet().stream().map(String::toString).collect(Collectors.toList());
  }

  /**
   * Returns Map of only commands which are accessible by user.
   */
  public CommandNameToInfo getUseraccessibleCommandsInfo() {
    return CommandNameToInfo.of(strToCommand.entrySet().stream().filter(e -> e.getValue().isUserAccessible()).collect(Collectors.toMap(k -> k.getValue().getName(), v -> v.getValue().getInfo())));
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CommandManager obj)) return false;
    if (this == o) return true;
    return this.getCommandNames().equals(obj.getCommandNames());
  }
}