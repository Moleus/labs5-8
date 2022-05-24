package commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Immutable data class which stores basic information about a console command.
 * Use factory method for new instances creation.
 */
@Data
@RequiredArgsConstructor(staticName = "of")
final public class CommandInfo implements Serializable {
  private static final long serialVersionUID = 8_0;
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
  private final CommandType type;

  /**
   * Returns execution mode.
   */
  private final ExecutionMode executionMode;
}
