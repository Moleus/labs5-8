package server.generated.repository;

import perform.annotations.Repository;
import perform.database.repository.CrudRepository;
import server.authorization.User;

@Repository
public interface UserReopsitory extends CrudRepository<User> {
  User findByLogin(String login);
}
