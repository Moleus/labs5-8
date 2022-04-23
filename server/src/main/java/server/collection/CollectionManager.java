package server.collection;

import collection.CollectionChangelist;
import collection.CollectionWrapper;
import exceptions.ElementNotFoundException;
import model.data.Model;

public interface CollectionManager<T extends Model> {
  long add(T entity);

  boolean update(T entity);

  CollectionWrapper<T> getFullCollection();

  CollectionChangelist<T> getChangesNewerThan(long version);

  void loadCollection();

  void clear();

  void removeById(long id) throws ElementNotFoundException;

  boolean removeLower(T upperBoundEntity);

  T getMax() throws ElementNotFoundException;

  T getMin() throws ElementNotFoundException;

  T getById(long id) throws ElementNotFoundException;
}