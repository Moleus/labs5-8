package communication;

import exceptions.InvalidCredentialsException;
import exceptions.UserAlreadyExistsException;
import user.User;

import java.io.IOException;

public class ClientAuthenticator implements Authenticator {

  private final Exchanger exchanger;

  public ClientAuthenticator(Exchanger exchanger) {
    this.exchanger = exchanger;
  }

  @Override
  public void register(User user) throws UserAlreadyExistsException, IOException {
    exchanger.requestRegister(user);
    try {
      exchanger.validateRegister();
    } catch (InvalidCredentialsException e) {
      throw new UserAlreadyExistsException(user.getLogin());
    }
  }

  @Override
  public void login(User user) throws InvalidCredentialsException, IOException {
    exchanger.requestLogin(user);
    exchanger.validateLogin();
  }
}