package collection;

import lombok.ToString;

import java.io.Serializable;
import java.util.NavigableMap;

@ToString
public class CollectionChangelist implements Serializable {
  private long latestVersion;
  private final NavigableMap<Long, CollectionChange> changelist;

  public CollectionChangelist(long version, NavigableMap<Long, CollectionChange> initialChangelsit) {
    latestVersion = version;
    changelist = initialChangelsit;
  }

  public void newChange(CollectionChange change) {
    changelist.put(++latestVersion, change);
  }

  public long getLatestVersion() {
    return latestVersion;
  }

  public CollectionChangelist sliceNewerThan(long version) {
    return new CollectionChangelist(latestVersion, changelist.tailMap(version, false));
  }

  public NavigableMap<Long, CollectionChange> getChangelist() {
    return changelist;
  }
}
