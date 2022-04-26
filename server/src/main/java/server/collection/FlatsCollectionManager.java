package server.collection;

import lombok.extern.log4j.Log4j2;
import model.data.Flat;
import server.authentication.UserManager;
import server.generated.repository.FlatRepository;
import user.User;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Manager class which stores collection and provides an API to interact with it.
 */
@Log4j2
public class FlatsCollectionManager extends GenericCollectionManager<Flat> {
  private UserManager userManager;

  public FlatsCollectionManager(FlatRepository flatRepository, UserManager userManager) {
    super(new FlatChangesTracker(), flatRepository);
    this.userManager = userManager;
  }

  @Override
  public long add(Flat entity, User user) {
    entity.setCreationDate(LocalDate.now());
    entity.setUserId(getIdFromDb(user));
    long id = super.add(entity);
    entity.setId(id);
    return id;
  }

  protected Predicate<Flat> isOwner(User user) {
    long userId = getIdFromDb(user);
    return flat -> Objects.equals(flat.getUserId(), userId);
  }

  private long getIdFromDb(User user) {
    return userManager.findByLogin(user.getLogin()).getId();
  }
}