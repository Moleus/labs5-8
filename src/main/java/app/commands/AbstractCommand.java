package app.commands;

public abstract class AbstractCommand implements Command {
  final CommandInfo info;

  public AbstractCommand(CommandInfo commandInfo) {
    this.info = commandInfo;
  }

  @Override
  public abstract ExecutionResult execute(ExecutionPayload payload);

  @Override
  public final String getName() {
    return info.getName();
  }

  @Override
  public final String getDescription() {
    return info.getDescription();
  }

  @Override
  public final boolean isUserAccessible() {
    return info.isUserAccessible();
  }

  @Override
  public final int getArgsCount() {
    return info.getArgsCount();
  }

  @Override
  public final boolean isHasComplexArgs() {
    return info.isHasComplexArgs();
  }

  @Override
  public CommandInfo getInfo() {
    return info;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AbstractCommand)) return false;
    AbstractCommand oc = (AbstractCommand) o;
    if (this == o) return true;
    return this.info.equals(oc.getInfo());
  }

  @Override
  public int hashCode() {
    return info.hashCode();
  }
}