package app.commands;

import lombok.Getter;

final public class CommandInfo {
  @Getter
  private final String name;
  @Getter
  private final String description;
  @Getter
  private final boolean userAccessible;
  @Getter
  private final int argsCount;
  @Getter
  private final boolean hasComplexArgs;

  private CommandInfo(String name, String description, boolean userAccessible, int argsCount, boolean hasComplexArgs) {
    this.name = name;
    this.description = description;
    this.argsCount = argsCount;
    this.userAccessible = userAccessible;
    this.hasComplexArgs = hasComplexArgs;
  }

  public static CommandInfo valueOf(String name, String description, boolean userAccessible, int argsCount, boolean hasComplexArgs) {
    return new CommandInfo(name, description, userAccessible, argsCount, hasComplexArgs);
  }
}
