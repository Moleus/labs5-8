package commands.pcommands;

import commands.*;
import communication.Authenticator;
import exceptions.InvalidCredentialsException;
import user.User;

import java.io.IOException;

public class Login extends AbstractCommand {
  private final Authenticator authenticator;

  public Login(Authenticator authenticator) {
    super(CommandInfo.of("login", "Log into existing account", true, 0, CommandType.AUTHENTICATION, ExecutionMode.CLIENT));
    this.authenticator = authenticator;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    User user = payload.getUser();
    try {
      authenticator.login(user);
    } catch (InvalidCredentialsException | IOException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }
    return ExecutionResult.valueOf(true, "Success. You are logged in.");
  }
}
