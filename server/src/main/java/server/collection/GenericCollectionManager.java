package server.collection;

import collection.CollectionChangelist;
import collection.CollectionWrapper;
import exceptions.ElementNotFoundException;
import model.data.Flat;
import model.data.Model;
import perform.database.repository.CrudRepository;
import user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class GenericCollectionManager<T extends Model> implements CollectionManager<T> {
  protected final Set<T> objectsCollection = ConcurrentHashMap.newKeySet();
  private final ChangesTracker<T> changesTracker;
  private final CrudRepository<T> entityRepository;
  private final LocalDateTime creationDateTime = LocalDateTime.now();

  public GenericCollectionManager(ChangesTracker<T> changesTracker, CrudRepository<T> entityRepository) {
    this.changesTracker = changesTracker;
    this.entityRepository = entityRepository;
  }

  /**
   * Adds new {@link Model} to collection.
   * Throws {@link IllegalArgumentException} if object is already in collection.
   *
   * @param object Model instance
   */
  protected long add(T object) {
    long id = entityRepository.save(object);
    object.setId(id);
    if (!objectsCollection.add(object)) {
      throw new IllegalArgumentException("Trying to add duplicate object");
    }
    changesTracker.track(Set.of(object), DiffAction.ADD);
    return id;
  }

  @Override
  public boolean update(T entity, User user) {
    if (!isOwner(user).test(entity) || !entityRepository.update(entity)) {
      return false;
    }
    changesTracker.track(Set.of(entity), DiffAction.UPDATE);
    return true;
  }

  /**
   * Removes all elements from collection.
   *
   * @see LinkedHashSet#clear()
   */
  @Override
  public void clear(User user) {
    changesTracker.track(filter(isOwner(user)), DiffAction.REMOVE);
    for (T entity : objectsCollection.stream().filter(isOwner(user)).collect(Collectors.toList())) {
      objectsCollection.remove(entity);
      entityRepository.delete(entity);
    }
  }

  public abstract long add(Flat entity, User user);

  protected abstract Predicate<T> isOwner(User user);

  /**
   * Returns max {@link Model} from collection.
   *
   * @throws exceptions.ElementNotFoundException if collection is empty.
   */
  @Override
  public T getMax() throws exceptions.ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new exceptions.ElementNotFoundException("Can't get max. Collection is empty");
    }
    return Collections.max(objectsCollection);
  }

  /**
   * Returns min {@link Model} from collection.
   *
   * @throws exceptions.ElementNotFoundException if collection is empty.
   */
  @Override
  public T getMin() throws exceptions.ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new exceptions.ElementNotFoundException("Can't get min. Collection is empty");
    }
    return Collections.min(objectsCollection);
  }

  /**
   * Returns {@link Model} from collection by id.
   *
   * @param id Id to get {@link Model} object from collection.
   * @throws ElementNotFoundException if there is no entry with such id in collection.
   */
  @Override
  public T getById(long id) throws ElementNotFoundException {
    return objectsCollection.stream().filter(flat -> Objects.equals(id, flat.getId())).findAny().orElseThrow(() -> new ElementNotFoundException("No element with such id in collection"));
  }

  /**
   * Removes {@link Model} from collection by id.
   *
   * @param id Id to remove {@link Model} object from collection.
   * @throws ElementNotFoundException if there is no entry with such id in collection.
   */
  @Override
  public void removeById(long id, User user) throws ElementNotFoundException {
    Predicate<T> filter = isOwner(user).and(flat -> Objects.equals(id, flat.getId()));
    changesTracker.track(filter(filter), DiffAction.REMOVE);

    if (!objectsCollection.removeIf(filter)) {
      throw new ElementNotFoundException("No element with such id in collection or it's not owned by you.");
    }
  }

  /**
   * Removes all entries which are less than passed {@link Model} object.
   *
   * @param upperBoundModel {@link Model} to compare with.
   * @return true if removed. False if nothing changed.
   */
  @Override
  public boolean removeLower(T upperBoundModel, User user) {
    Predicate<T> predicate = isOwner(user).and(flat -> flat.compareTo(upperBoundModel) < 0);
    changesTracker.track(filter(predicate), DiffAction.REMOVE);
    return objectsCollection.removeIf(predicate);
  }

  private Set<T> filter(Predicate<T> predicate) {
    return objectsCollection.stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Returns wrapper object with exact copy of current collection and creationDate
   */
  @Override
  public CollectionWrapper<T> getFullCollection() {
    return CollectionWrapper.of(Set.copyOf(objectsCollection), creationDateTime, changesTracker.getLatestVersion());
  }

  /**
   * Returns changelist with changes whose version is newer than specified parameter.
   */
  @Override
  public CollectionChangelist<T> getChangesNewerThan(long version) {
    return changesTracker.getNewerThan(version);
  }

  /**
   * Loads collection from storage.
   */
  @Override
  public void loadCollection() {
    this.objectsCollection.addAll(entityRepository.findAll());
  }
}
