package server.generated.repository;

import perform.annotations.Repository;
import perform.database.repository.CrudRepository;
import user.User;

@Repository
public interface UserRepository extends CrudRepository<User> {
  User findByLogin(String login);
}
