package commands;

import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractCommand implements Command, Serializable {
  @Serial
  private static final long serialVersionUID = 102L;
  final CommandInfo info;

  public AbstractCommand(CommandInfo commandInfo) {
    this.info = commandInfo;
  }

  /** {@inheritDoc} */
  @Override
  public abstract ExecutionResult execute(ExecutionPayload payload);

  /** {@inheritDoc} */
  @Override
  public final String getName() {
    return info.getName();
  }

  /** {@inheritDoc} */
  @Override
  public final String getDescription() {
    return info.getDescription();
  }

  @Override
  public final boolean isUserAccessible() {
    return info.isUserAccessible();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getArgsCount() {
    return info.getArgsCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final CommandType getType() {
    return info.getType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CommandInfo getInfo() {
    return info;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AbstractCommand oc)) return false;
    if (this == o) return true;
    return this.info.equals(oc.getInfo());
  }

  @Override
  public int hashCode() {
    return info.hashCode();
  }
}