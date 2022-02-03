package app.commands;

public interface Command {
  /**
   * 
   * @return Object which contains an execution status boolean and a result string
   */
  ExecutionResult execute(ExecutionPayload payload);

  String getName();
  String getDescription();
  boolean isUserAccessible();
  int getArgsCount();
  boolean isHasComplexArgs();
  CommandInfo getInfo();
}
