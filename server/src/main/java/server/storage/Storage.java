package server.storage;

import model.data.Model;
import server.exceptions.CollectionCorruptedException;
import server.exceptions.StorageAccessException;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Set;

/**
 * Provides API to load or save a collection from/to storage.
 */
public interface Storage {
  /**
   * @return new collection object
   * @throws CollectionCorruptedException if some data in storage is missing or can't be parsed to create new collection object.
   * @throws StorageAccessException       if storage is not accessible.
   */
  AbstractMap.SimpleEntry<LocalDateTime, Set<Model>> loadCollection() throws CollectionCorruptedException, StorageAccessException;

  /**
   * Saves collection in storage
   *
   * @throws StorageAccessException if storage is not accessible.
   */
  void saveCollection(AbstractMap.SimpleEntry<LocalDateTime, Set<Model>> collection) throws StorageAccessException;
}
