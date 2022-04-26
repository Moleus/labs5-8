package server.collection;

import collection.CollectionChangelist;
import collection.CollectionWrapper;
import exceptions.ElementNotFoundException;
import model.data.Model;
import user.User;

public interface CollectionManager<T extends Model> {
  long add(T entity);

  boolean update(T entity, User user);

  boolean removeLower(T upperBoundModel, User user);

  CollectionWrapper<T> getFullCollection();

  CollectionChangelist<T> getChangesNewerThan(long version);

  void loadCollection();

  void clear(User user);

  void removeById(long id, User user) throws ElementNotFoundException;

  T getMax() throws ElementNotFoundException;

  T getMin() throws ElementNotFoundException;

  T getById(long id) throws ElementNotFoundException;
}