package collection;

import lombok.ToString;
import model.data.Model;

import java.io.Serializable;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicLong;

@ToString
public class CollectionChangelist<T extends Model> implements Serializable {
  private static final long serialVersionUID = 8_0;
  private final AtomicLong latestVersion;
  private final NavigableMap<Long, CollectionChange<T>> changelist;

  public CollectionChangelist(long version, NavigableMap<Long, CollectionChange<T>> initialChangelist) {
    latestVersion = new AtomicLong(version);
    changelist = initialChangelist;
  }

  public void newChange(CollectionChange<T> change) {
    changelist.put(latestVersion.incrementAndGet(), change);
  }

  public void loadChanges(List<CollectionChange<T>> changes) {
    for (CollectionChange<T> change : changes) {
      newChange(change);
    }
  }

  public long getLatestVersion() {
    return latestVersion.get();
  }

  public CollectionChangelist<T> sliceNewerThan(long version) {
    return new CollectionChangelist<T>(latestVersion.get(), changelist.tailMap(version, false));
  }

  public NavigableMap<Long, CollectionChange<T>> getChangelist() {
    return changelist;
  }
}