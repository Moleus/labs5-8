package collection;

import lombok.ToString;
import model.data.Model;

import java.io.Serializable;
import java.util.List;
import java.util.NavigableMap;

@ToString
public class CollectionChangelist<T extends Model> implements Serializable {
  private long latestVersion;
  private final NavigableMap<Long, CollectionChange<T>> changelist;

  public CollectionChangelist(long version, NavigableMap<Long, CollectionChange<T>> initialChangelsit) {
    latestVersion = version;
    changelist = initialChangelsit;
  }

  public void newChange(CollectionChange<T> change) {
    changelist.put(++latestVersion, change);
  }

  public void loadChanges(List<CollectionChange<T>> changes) {
    for (CollectionChange<T> change : changes) {
      newChange(change);
    }
  }

  public long getLatestVersion() {
    return latestVersion;
  }

  public CollectionChangelist<T> sliceNewerThan(long version) {
    return new CollectionChangelist<T>(latestVersion, changelist.tailMap(version, false));
  }

  public NavigableMap<Long, CollectionChange<T>> getChangelist() {
    return changelist;
  }
}