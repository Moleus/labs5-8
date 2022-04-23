package server.authorization;

import server.generated.repository.UserRepository;

public class UserManager {
  public UserRepository userRepository;

  public UserManager(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUserBy(String login) {
    return userRepository.findByLogin(login);
  }

  public void save(User user) {
    userRepository.save(user);
  }
}
