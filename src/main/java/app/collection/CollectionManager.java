package app.collection;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import app.collection.data.Flat;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.ElementNotFoundException;
import app.exceptions.StorageAccessException;
import app.storage.Storage;

/**
 * Manager class which stores collection and provides an API to interact with it.
 */
public class CollectionManager {
  //  Set s = Collections.synchronizedSet(new LinkedHashSet(...));
  private final LinkedHashSet<Flat> objectsCollection = new LinkedHashSet<>();
  private final LocalDateTime creationDateTime;
  private final Storage storageManager;

  public CollectionManager(Storage storageManager) {
    this.creationDateTime = LocalDateTime.now();
    this.storageManager = storageManager;
  }

  /**
   * Saves collection in storage.
   * @throws StorageAccessException if storage is not accessible.
   */
  public void saveCollection() throws StorageAccessException {
    storageManager.saveCollection(objectsCollection);
  }

  /**
   * Loads collection from storage.
   * @throws CollectionCorruptedException if some data in storage is missing or can't be parsed to create new collection object.
   * @throws StorageAccessException if storage is not accessible.
   */
  public void loadCollection() throws CollectionCorruptedException, StorageAccessException {
    this.objectsCollection.addAll(storageManager.loadCollection());
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
  }

  /**
   * Removes all elements from collection.
   * @see LinkedHashSet#clear()
   */
  public void clear() {
    objectsCollection.clear();
  }

  /**
   * Returns collection creation time
   */
  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  /**
   * Returns collection size.
   * @see LinkedHashSet#size()
   */
  public int getSize() {
    return objectsCollection.size();
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
    if (!objectsCollection.removeIf(flat -> Objects.equals(id, flat.getId()))) {
      throw new ElementNotFoundException("No element with such id in collection");
    }
  }

  /**
   * Returns max {@link Flat} from collection.
   * @throws ElementNotFoundException if collection is empty.
   */
  public Flat getMax() throws ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new ElementNotFoundException("Can't get max. Collection is empty");
    }
    return Collections.max(objectsCollection);
  }

  /**
   * Returns min {@link Flat} from collection.
   * @throws ElementNotFoundException if collection is empty.
   */
  public Flat getMin() throws ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new ElementNotFoundException("Can't get min. Collection is empty");
    }
    return Collections.min(objectsCollection);
  }

  /**
   * Removes all entries which are less than passed {@link Flat} object.
   * @param upperBoundFlat {@link Flat} to compare with.
   * @return true if removed. False if nothing changed.
   */
  public boolean removeLower(Flat upperBoundFlat) {
    return objectsCollection.removeIf(flat -> flat.compareTo(upperBoundFlat) < 0);
  }

  /**
   * Returns array of {@link Flat} which name contains passed string.
   * @param filter string to lookup in names.
   */
  public Flat[] filterContainsName(String filter) {
    return objectsCollection.stream().sorted().filter(flat -> flat.getName().contains(filter)).toArray(Flat[]::new);
  }

  /**
   * Returns a {@link Set} of unique {@link Long} values got by {@link Flat#getNumberOfRooms()} from each entry.
   */
  public Set<Long> getUniqueNumberOfRooms() {
    return objectsCollection.stream().map(Flat::getNumberOfRooms).collect(Collectors.toSet());
  }

  /**
   * Returns an array of {@link Boolean} values in descending order got by {@link Flat#getNewness()} from each entry.
   */
  public Boolean[] getFieldDescendingNew() {
    return objectsCollection.stream().sorted(Comparator.reverseOrder()).map(Flat::getNewness).toArray(Boolean[]::new);
  }
}