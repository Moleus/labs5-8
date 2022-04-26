package commands.readers;

import user.User;

import java.io.IOException;

public class UserReader implements ObjectReader<User> {
  private final IOSource ioSource;

  public UserReader(IOSource IOSource) {
    this.ioSource = IOSource;
  }

  @Override
  public User read() throws IOException {
    User user = new User();
    ioSource.print("Enter login: ");
    String login = ioSource.readLine();
    user.setLogin(login);
    ioSource.print("Enter password: ");
    byte[] password = ioSource.readPassword();
    user.setPassword(password);
    return user;
  }
}
