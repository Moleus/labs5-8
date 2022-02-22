package commands;

import lombok.Data;

@Data(staticConstructor = "of")
public class ExecutionPayload {
  private final String inlineArg;
  private final Object[] dataValues;
}
