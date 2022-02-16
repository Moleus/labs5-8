package app.commands.pcommands;

import app.commands.*;

import java.util.Map;
import java.util.stream.Collectors;

public final class Help extends AbstractCommand {
  private final CommandManager commandManager;

  public Help(CommandManager commandManager) {
    super(CommandInfo.valueOf("help", "Shows info about accessible commands", true, 0, false));
    this.commandManager = commandManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Map<String, Command> nameToCommand = commandManager.getUserAccessibleCommands();
    int longestNameLength = nameToCommand.keySet().stream().map(String::length).max(Integer::compareTo).get();
    String commandsWithDescriptions = nameToCommand.values().stream()
        .map(c -> String.format("%" + longestNameLength + "s: %s",
            c.getName(), c.getDescription()))
        .collect(Collectors.joining(System.lineSeparator()));
    return ExecutionResult.valueOf(true, commandsWithDescriptions);
  }
}