package commands.pcommands;

import commands.*;
import communication.Authenticator;
import exceptions.InvalidCredentialsException;

public class Login extends AbstractCommand {
  private final Authenticator authenticator;

  public Login(Authenticator authenticator) {
    super(CommandInfo.of("login", "Log into existing account", true, 1, CommandType.AUTHENTICATION, ExecutionMode.CLIENT));
    this.authenticator = authenticator;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String login = payload.getInlineArg();
    char[] password = payload.getMaskedInput();

    try {
      authenticator.login(login, password);
    } catch (InvalidCredentialsException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }
    return ExecutionResult.valueOf(true, "Success. You are logged in.");
  }
}
