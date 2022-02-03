package app.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.common.CommandResponse;
import app.common.Request;
import app.common.Response;
import app.exceptions.CommandNotRegisteredException;

public class CommandManager {
  private final Map<String, Command> strToCommand;
  
  public CommandManager() {
    this.strToCommand = new HashMap<>();
  }

  public void registerCommands(Command ... commands) {
    // add commands to HashMap
    for (Command cmd : commands) {
      registerCommand(cmd);
    }
  }

  private boolean isInCommands(String strCommand) {
    return strToCommand.get(strCommand) != null;
  }
  
  private void registerCommand(Command cmd) {
    // TODO: some null checks 
    // TODO: throw exception that has a wrong type
    String commandName = cmd.getName();
    // TODO: Check if is a duplicate and throw command duplicate exception
    if (isInCommands(commandName)) { // found a duplicate
      System.err.println("Command already registered");
      return;
    }
    strToCommand.put(commandName, cmd);
  }
  
  public Response executeCommand(Request request) {
    // TODO: Do we need some validation here or on ka client side?
    // validation TODO: Check if command is in hashMap
    // !!! validation TODO: Check if command structure: args, ...
    // TODO: when executing script - check that it's not looped

    // TODO: Команда не должна работать с Request/Response. Она работает с параметрами и возвращает Строку как результат
    String commandName = request.getCommandName();
    if (!isRegistered(commandName)) {
      return CommandResponse.valueOf(ExecutionResult.valueOf(false, "Invalid command"));
    }
    ExecutionPayload payload = request.getExecutionPayload();
    ExecutionResult result = strToCommand.get(commandName).execute(payload);

    return CommandResponse.valueOf(result);
  }

  public boolean isRegistered(String commandName) {
    return strToCommand.containsKey(commandName);
  }

  public Command getCommand(String commandName) throws CommandNotRegisteredException {
    Command command = strToCommand.get(commandName);
    if (command == null) throw new CommandNotRegisteredException(commandName);
    return command;
  }

  public List<String> getCommandNames() {
    return strToCommand.keySet().stream().map(String::toString).collect(Collectors.toList());
  }

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