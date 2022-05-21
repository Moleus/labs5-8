package commands;

import java.util.List;

public interface CommandManager {
  boolean isRegistered(String commandName);

  void registerCommands(Command... commands);

  void addCommandsInfo(CommandNameToInfo commandNameToInfo);

  ExecutionResult executeCommand(ExecutionPayload payload);

  List<String> getCommandNames();

  CommandNameToInfo getUserAccessibleCommandsInfo();
}
