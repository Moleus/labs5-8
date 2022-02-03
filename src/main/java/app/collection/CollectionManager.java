package app.collection;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;

import app.collection.data.Flat;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.StorageAccessException;
import app.storage.Storage;

/**
 * 
 * TODO: {@code E} stands for a Type of a custom data object, specified in Lab task text. For example {@code Flat}
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

  public void saveCollection(LinkedHashSet<Flat> myCollection) throws StorageAccessException {
    storageManager.saveCollection(myCollection);
    // stdout saved;
  }

  public void loadCollection() throws CollectionCorruptedException, StorageAccessException {
    this.objectsCollection.addAll(storageManager.loadCollection());
  }
  
  public String info() {
    return null;
  }

  public void add(Flat object) {
    // TODO: throw exception if object already exists
    objectsCollection.add(object);
  }

  public void sort() {
    // sort collection
  }

  public void clear() {
    // clears collection
  }

  public void removeLast() {
    // removes last added element from collection 
  }

  public LocalDateTime getCreatedDateTime() {
    return creationDateTime;
  }

  /**
   * @param id
   * @return Elem {@code E} by id from collection
   * @throws NoSuchIdException
   */
  public Flat getById(Integer id) {
    // returns object by id
    return null;
  }

  public void removeById(Integer id) {}

}