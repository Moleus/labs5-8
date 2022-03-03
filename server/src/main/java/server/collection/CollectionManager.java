package server.collection;

import collection.CollectionChange;
import collection.CollectionChangelist;
import collection.CollectionWrapper;
import exceptions.ElementNotFoundException;
import lombok.extern.log4j.Log4j2;
import model.data.Flat;
import server.exceptions.CollectionCorruptedException;
import server.exceptions.StorageAccessException;
import server.storage.Storage;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manager class which stores collection and provides an API to interact with it.
 */
@Log4j2
public class CollectionManager {
  //  Set s = Collections.synchronizedSet(new LinkedHashSet(...));
  private final Set<Flat> objectsCollection = new LinkedHashSet<>();
  private LocalDateTime creationDateTime = LocalDateTime.now();
  private final CollectionChangelist changelist = new CollectionChangelist(0L, new TreeMap<>());
  private final Storage storageManager;

  public CollectionManager(Storage storageManager) {
    this.storageManager = storageManager;
  }

  /**
   * Returns wrapper object with exact copy of current collection and creationDate
   */
  public CollectionWrapper getNewestCollection() {
    return CollectionWrapper.of(new LinkedHashSet<>(objectsCollection), creationDateTime, changelist.getLatestVersion());
  }

  public CollectionChangelist getChangesNewerThan(long version) {
    return changelist.sliceNewerThan(version);
  }

  /**
   * Saves collection in storage.
   *
   * @throws StorageAccessException if storage is not accessible.
   */
  public void saveCollection() throws StorageAccessException {
    storageManager.saveCollection(new AbstractMap.SimpleEntry<>(creationDateTime, objectsCollection));
  }

  /**
   * Loads collection from storage.
   * @throws CollectionCorruptedException if some data in storage is missing or can't be parsed to create new collection object.
   * @throws StorageAccessException if storage is not accessible.
   */
  public void loadCollection() throws CollectionCorruptedException, StorageAccessException {
    AbstractMap.SimpleEntry<LocalDateTime, Set<Flat>> dateToCollection = storageManager.loadCollection();
    creationDateTime = dateToCollection.getKey();
    this.objectsCollection.addAll(dateToCollection.getValue());
  }

  /**
   * Adds new {@link Flat} to collection.
   * Throws {@link IllegalArgumentException} if object is already in collection.
   * @param object Flat instance
   */
  public void add(Flat object) {
    if (!objectsCollection.add(object)) {
      throw new IllegalArgumentException("Trying to add dublicate object");
    }
    addToChangelist(Set.of(object), DiffAction.ADD);
  }

  /**
   * Removes all elements from collection.
   * @see LinkedHashSet#clear()
   */
  public void clear() {
    addToChangelist(getMatches(t -> true), DiffAction.REMOVE);
    objectsCollection.clear();
  }

  /**
   * Returns max {@link Flat} from collection.
   *
   * @throws exceptions.ElementNotFoundException if collection is empty.
   */
  public Flat getMax() throws exceptions.ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new exceptions.ElementNotFoundException("Can't get max. Collection is empty");
    }
    return Collections.max(objectsCollection);
  }

  /**
   * Returns min {@link Flat} from collection.
   *
   * @throws exceptions.ElementNotFoundException if collection is empty.
   */
  public Flat getMin() throws exceptions.ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new exceptions.ElementNotFoundException("Can't get min. Collection is empty");
    }
    return Collections.min(objectsCollection);
  }

  /**
   * Returns {@link Flat} from collection by id.
   * @param id Id to get {@link Flat} object from collection.
   * @throws ElementNotFoundException if there is no entry with such id in collection.
   */
  public Flat getById(Integer id) throws ElementNotFoundException {
    return objectsCollection.stream().filter(flat -> Objects.equals(id, flat.getId())).findAny().orElseThrow(() -> new ElementNotFoundException("No element with such id in collection"));
  }

  /**
   * Removes {@link Flat} from collection by id.
   * @param id Id to remove {@link Flat} object from collection.
   * @throws ElementNotFoundException if there is no entry with such id in collection.
   */
  public void removeById(Integer id) throws ElementNotFoundException {
    Predicate<Flat> filter = flat -> Objects.equals(id, flat.getId());
    addToChangelist(getMatches(filter), DiffAction.REMOVE);

    if (!objectsCollection.removeIf(filter)) {
      throw new ElementNotFoundException("No element with such id in collection");
    }
  }

  /**
   * Removes all entries which are less than passed {@link Flat} object.
   *
   * @param upperBoundFlat {@link Flat} to compare with.
   * @return true if removed. False if nothing changed.
   */
  public boolean removeLower(Flat upperBoundFlat) {
    Predicate<Flat> filter = flat -> flat.compareTo(upperBoundFlat) < 0;
    addToChangelist(getMatches(filter), DiffAction.REMOVE);
    return objectsCollection.removeIf(filter);
  }

  private Set<Flat> getMatches(Predicate<Flat> filter) {
    Map<Boolean, List<Flat>> changedToList = objectsCollection.stream().collect(Collectors.partitioningBy(filter));
    return Set.copyOf(changedToList.get(Boolean.TRUE));
  }

  private void addToChangelist(Set<Flat> changes, DiffAction action) {
    if (changes.isEmpty()) {
      return;
    }
    switch (action) {
      case ADD -> changelist.newChange(CollectionChange.of(Collections.emptySet(), changes));
      case REMOVE -> changelist.newChange(CollectionChange.of(changes, Collections.emptySet()));
      case UPDATE -> changelist.newChange(CollectionChange.of(changes, changes));
    }
  }
}