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

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CommandInfo)) return false;
    CommandInfo obj = (CommandInfo) o;
    if (this == o) return true;
    return  this.name.equals(obj.getName()) &&
            this.description.equals(obj.getDescription()) &&
            this.userAccessible == obj.isUserAccessible() &&
            this.argsCount      == obj.getArgsCount() &&
            this.hasComplexArgs == obj.isHasComplexArgs();
  }

  @Override
  public int hashCode() {
    int hash = name.hashCode();
    hash ^= description.hashCode();
    hash ^= userAccessible ? 1 : 0;
    hash ^= hasComplexArgs ? 1 : 0;
    return hash;
  }
}
