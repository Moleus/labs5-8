package app.collection;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import app.collection.data.Flat;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.ElementNotFoundException;
import app.exceptions.StorageAccessException;
import app.storage.Storage;

public class CollectionManager {
  //  Set s = Collections.synchronizedSet(new LinkedHashSet(...));
  private final LinkedHashSet<Flat> objectsCollection = new LinkedHashSet<>();
  private final LocalDateTime creationDateTime;
  private final Storage storageManager;

  public CollectionManager(Storage storageManager) {
    this.creationDateTime = LocalDateTime.now();
    this.storageManager = storageManager;
  }

  public void saveCollection() throws StorageAccessException {
    storageManager.saveCollection(objectsCollection);
  }

  public void loadCollection() throws CollectionCorruptedException, StorageAccessException {
    this.objectsCollection.addAll(storageManager.loadCollection());
  }
  
  public void add(Flat object) {
    // TODO: throw exception if object already exists
    objectsCollection.add(object);
  }

  public void clear() {
    objectsCollection.clear();
  }

  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  public int getSize() {
    return objectsCollection.size();
  }

  public Flat getById(Integer id) throws ElementNotFoundException {
    return objectsCollection.stream().filter(flat -> Objects.equals(id, flat.getId())).findAny().orElseThrow(() -> new ElementNotFoundException("No element with such id in collection"));
  }

  public void removeById(Integer id) throws ElementNotFoundException {
    if (!objectsCollection.removeIf(flat -> Objects.equals(id, flat.getId()))) {
      throw new ElementNotFoundException("No element with such id in collection");
    }
  }

  public Flat getMax() throws ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new ElementNotFoundException("Can't get max. Collection is empty");
    }
    return Collections.max(objectsCollection);
  }

  public Flat getMin() throws ElementNotFoundException {
    if (objectsCollection.isEmpty()) {
      throw new ElementNotFoundException("Can't get min. Collection is empty");
    }
    return Collections.min(objectsCollection);
  }

  public boolean removeLower(Flat upperBoundFlat) {
    return objectsCollection.removeIf(flat -> flat.compareTo(upperBoundFlat) < 0);
  }

  public Flat[] filterContainsName(String filter) {
    return objectsCollection.stream().filter(flat -> flat.getName().contains(filter)).toArray(Flat[]::new);
  }

  public Set<Long> getUniqueNumberOfRooms() {
    return objectsCollection.stream().map(Flat::getNumberOfRooms).collect(Collectors.toSet());
  }

  public Boolean[] getFieldDescendingNew() {
    return objectsCollection.stream().sorted(Comparator.reverseOrder()).map(Flat::getNewness).toArray(Boolean[]::new);
  }
}