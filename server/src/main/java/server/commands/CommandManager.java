package server.commands;

import commands.Command;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import communication.CommandResponse;
import communication.Request;
import communication.Response;

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
   * @param request data-object with parameters required for command execution.
   * @return {@link Response} data-object with execution result.
   */
  public Response executeCommand(Request request) {
    String commandName = request.getCommandName();
    ExecutionPayload payload = request.getExecutionPayload();
    if (!isRegistered(commandName)) {
      return CommandResponse.valueOf(ExecutionResult.valueOf(false, "Invalid command"));
    }
    ExecutionResult result = strToCommand.get(commandName).execute(payload);
    return CommandResponse.valueOf(result);
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
  public Map<String, Command> getUserAccessibleCommands() {
    return strToCommand.entrySet().stream().filter(e -> e.getValue().isUserAccessible()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CommandManager)) return false;
    if (this == o) return true;
    CommandManager obj = (CommandManager) o;
    return this.getCommandNames().equals(obj.getCommandNames());
  }
}