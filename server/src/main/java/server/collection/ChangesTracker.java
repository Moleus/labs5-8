package server.collection;

import collection.CollectionChange;
import collection.CollectionChangelist;
import collection.CollectionWrapper;
import model.data.Model;

import java.util.List;
import java.util.Set;

public interface ChangesTracker<T extends Model> {
  CollectionWrapper<T> getNewest();

  CollectionChangelist<T> getNewerThan(long version);

  void track(Set<T> changes, DiffAction operation);

  /**
   * Posible use-case is loading this changes from a db table on server start-up.
   * Loads changes to tracker.
   */
  void loadChanges(List<CollectionChange<T>> changes);
}
