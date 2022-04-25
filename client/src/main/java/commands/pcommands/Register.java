package commands.pcommands;

import commands.*;
import communication.Authenticator;
import exceptions.InvalidCredentialsException;
import exceptions.UserAlreadyExistsException;
import user.User;

import java.io.IOException;

public class Register extends AbstractCommand {
  private final Authenticator authenticator;

  public Register(Authenticator authenticator) {
    super(CommandInfo.of("register", "Create new user account", true, 0, CommandType.AUTHENTICATION, ExecutionMode.CLIENT));
    this.authenticator = authenticator;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object data = payload.getData();
    if (!(data instanceof User user)) {
      return ExecutionResult.valueOf(false, "Assumed to get a User from payload");
    }
    try {
      authenticator.register(user);
    } catch (UserAlreadyExistsException | InvalidCredentialsException | IOException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }
    return ExecutionResult.valueOf(true, "Registration completed successfully");
  }
}
