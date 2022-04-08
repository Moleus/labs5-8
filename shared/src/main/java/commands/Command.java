package commands;

public interface Command {
  /**
   * Executes a command with parameters specified in a payload.
   * @return data-object which contains an execution status boolean and a result message string
   */
  ExecutionResult execute(ExecutionPayload payload);

  /** See {@link CommandInfo#getName()} */
  String getName();
  /** See {@link CommandInfo#getDescription()} */
  String getDescription();
  /** See {@link CommandInfo#isUserAccessible()} */
  boolean isUserAccessible();
  /** See {@link CommandInfo#getArgsCount()} */
  int getArgsCount();
  /** See {@link CommandInfo#isHasComplexArgs()} */
  boolean isHasComplexArgs();
  /**
   * Returns command info object
   */
  CommandInfo getInfo();
}
