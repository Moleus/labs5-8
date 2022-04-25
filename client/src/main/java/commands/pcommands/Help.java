package commands.pcommands;

import commands.*;

import java.util.stream.Collectors;

public final class Help extends AbstractCommand {
  private final CommandManager commandManager;

  public Help(CommandManager commandManager) {
    super(CommandInfo.of("help", "Shows info about accessible commands", true, 0, CommandType.OTHER, ExecutionMode.CLIENT));
    this.commandManager = commandManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    CommandNameToInfo nameToCommand = commandManager.getUseraccessibleCommandsInfo();
    int longestNameLength = nameToCommand.keySet().stream().map(String::length).max(Integer::compareTo).orElse(27);
    String commandsWithDescriptions = nameToCommand.values().stream()
        .map(c -> String.format("%" + longestNameLength + "s: %s",
            c.getName(), c.getDescription()))
        .collect(Collectors.joining(System.lineSeparator()));
    return ExecutionResult.valueOf(true, commandsWithDescriptions);
  }
}