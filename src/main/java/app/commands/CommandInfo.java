package app.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Immutable data class which stores basic information about a console command.
 * Use factory method for new instances creation.
 */
@Data
@RequiredArgsConstructor(staticName = "valueOf")
final public class CommandInfo {
  /**
   * Returns the name of a command.
   */
  private final String name;
  /**
   * Returns description message for a command.
   */
  private final String description;
  /**
   * Returns true if command is accessible by a user, otherwise - false.
   */
  private final boolean userAccessible;
  /**
   * Returns number of inline args required by a command.
   */
  private final int argsCount;
  /**
   * Returns true if additional user input required, otherwise - false.
   */
  private final boolean hasComplexArgs;

  /**
   * Returns execution mode.
   */
  private final ExecutionMode executionMode;
}
