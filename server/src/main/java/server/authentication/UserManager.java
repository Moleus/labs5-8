package server.authentication;

import at.favre.lib.crypto.bcrypt.BCrypt;
import server.generated.repository.UserRepository;
import user.User;

public class UserManager {
  public final UserRepository userRepository;

  public UserManager(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean areCredentialsValid(User user) {
    if (user == null) return false;
    User storedUser = userRepository.findByLogin(user.getLogin());
    return storedUser != null && verify(user.getPassword(), storedUser.getPassword());
  }

  public boolean isUserUnique(User user) {
    if (user == null) return false;
    return (null == userRepository.findByLogin(user.getLogin()));
  }

  public long save(User user) {
    byte[] encryptedPass = BCrypt.withDefaults().hash(12, user.getPassword());
    user.setPassword(encryptedPass);
    long id = userRepository.save(user);
    user.setId(id);
    return id;
  }

  public User findByLogin(String login) {
    return userRepository.findByLogin(login);
  }

  private boolean verify(byte[] rawPass, byte[] encrypted) {
    return BCrypt.verifyer().verify(rawPass, encrypted).verified;
  }

}
