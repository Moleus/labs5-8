package server.collection;

import collection.CollectionChange;
import collection.CollectionChangelist;
import model.data.Flat;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

public class FlatChangesTracker implements ChangesTracker<Flat> {
  private final CollectionChangelist<Flat> changelist = new CollectionChangelist<>(0L, new ConcurrentSkipListMap<>());

  @Override
  public long getLatestVersion() {
    return changelist.getLatestVersion();
  }

  @Override
  public CollectionChangelist<Flat> getNewerThan(long version) {
    return changelist.sliceNewerThan(version);
  }

  @Override
  public void track(Set<Flat> changes, DiffAction operation) {
    if (changes.isEmpty()) {
      return;
    }
    switch (operation) {
      case ADD: changelist.newChange(CollectionChange.add(changes)); break;
      case REMOVE: changelist.newChange(CollectionChange.remove(changes)); break;
      case UPDATE: changelist.newChange(CollectionChange.update(changes)); break;
    }
  }

  @Override
  public void loadChanges(List<CollectionChange<Flat>> changes) {
    changelist.loadChanges(changes);
  }
}
