package app.commands;

public class ExecutionPayload {
  private final String inlineArg;
  private final Object dataObject;

  private ExecutionPayload(String inlineArg, Object dataObject) {
    this.inlineArg = inlineArg;
    this.dataObject = dataObject;
  }

  public static ExecutionPayload valueOf(String inlineArg, Object dataObject) {
    return new ExecutionPayload(inlineArg, dataObject);
  }

  public String getInlineArg() {
    return this.inlineArg;
  }

  public Object getDataObject() {
    return this.dataObject;
  }
}
