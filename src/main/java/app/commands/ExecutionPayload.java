package app.commands;

public class ExecutionPayload {
  private final String inlineArg;
  private final Object[] dataValues;

  private ExecutionPayload(String inlineArg, Object[] dataValues) {
    this.inlineArg = inlineArg;
    this.dataValues = dataValues;
  }

  public static ExecutionPayload valueOf(String inlineArg, Object[] dataValues) {
    return new ExecutionPayload(inlineArg, dataValues);
  }

  public String getInlineArg() {
    return this.inlineArg;
  }

  public Object[] getDataValues() {
    return this.dataValues;
  }
}
