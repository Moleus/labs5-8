package commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Manager class which stores all commands. Provieds an API to register new commands and execute them.
 */
public class CommandManagerImpl implements CommandManager {
  private final Map<String, Command> nameToCommand;
  private final Map<String, CommandInfo> nameToInfo;

  public CommandManagerImpl() {
    this.nameToCommand = new HashMap<>();
    this.nameToInfo = new HashMap<>();
  }

  /**
   * Registers passed commands in this manager.
   *
   * @param commands commands to register
   */
  @Override
  public void registerCommands(Command... commands) {
    for (Command cmd : commands) {
      registerCommand(cmd);
    }
  }

  /**
   * Checks if command is registered in command manager.
   */
  private boolean isInCommands(String strCommand) {
    return nameToCommand.get(strCommand) != null;
  }

  private void registerCommand(Command cmd) {
    Objects.requireNonNull(cmd);
    String commandName = cmd.getName();
    if (isInCommands(commandName)) { // found a duplicate
      System.err.printf("Command %s already registered%n", commandName);
      return;
    }
    nameToCommand.put(commandName, cmd);
    nameToInfo.put(commandName, cmd.getInfo());
  }

  /**
   * Use this if you want to store info about commands without providing executable command by itself.
   */
  @Override
  public void addCommandsInfo(CommandNameToInfo commandNameToInfo) {
    nameToInfo.putAll(commandNameToInfo);
  }

  /**
   * Executes command specified in request.
   *
   * @param payload data-object with parameters required for command execution.
   * @return {@link ExecutionResult} data-object with execution result.
   */
  @Override
  public ExecutionResult executeCommand(ExecutionPayload payload) {
    String commandName = payload.getCommandName();
    if (isRegistered(commandName)) {
      ExecutionResult result = nameToCommand.get(commandName).execute(payload);
      return result;
    }
    return ExecutionResult.valueOf(false, "Invalid command");
  }

  /**
   * Return true if command is registered in manager, otherwise - false.
   */
  @Override
  public boolean isRegistered(String commandName) {
    return nameToCommand.containsKey(commandName);
  }

  /**
   * Returns List of all registered commands names.
   */
  @Override
  public List<String> getCommandNames() {
    return nameToCommand.keySet().stream().map(String::toString).collect(Collectors.toList());
  }

  /**
   * Returns Map of only commands which are accessible by user.
   */
  @Override
  public CommandNameToInfo getUserAccessibleCommandsInfo() {
    return CommandNameToInfo.of(nameToInfo.entrySet().stream().filter(e -> e.getValue().isUserAccessible()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CommandManagerImpl)) return false;
    if (this == o) return true;
    return this.getCommandNames().equals(((CommandManagerImpl)o).getCommandNames());
  }
}