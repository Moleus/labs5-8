package communication.packaging;

import user.User;

import java.util.Optional;

public interface Request extends Message {
  Optional<User> getUser();
}