package commands;

import lombok.Data;
import model.ModelDto;
import user.User;

import java.io.Serializable;

@Data(staticConstructor = "of")
public class ExecutionPayload implements Serializable {
  private final String commandName;
  private final String inlineArg;
  private final ModelDto dataValues;
  private final User user;
}
