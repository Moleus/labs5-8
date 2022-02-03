package app.storage;

import java.util.LinkedHashSet;

import app.collection.data.Flat;
import app.exceptions.CollectionCorruptedException;
import app.exceptions.StorageAccessException;

/**
 * 
 * {@code E} stands for a type of a custom data object, specified in Lab task text. For example {@code Flat}
 */
public interface Storage {
  LinkedHashSet<Flat> loadCollection() throws CollectionCorruptedException, StorageAccessException;
  void saveCollection(LinkedHashSet<Flat> collection) throws StorageAccessException;
}
