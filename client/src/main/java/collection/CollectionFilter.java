package collection;

import model.data.Flat;
import model.data.Model;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionFilter {
  private Set<Flat> objectsCollection;
  private Long collectionVersion;
  private LocalDateTime creationDateTime;
  private final ChangesApplier<Flat> changesApplier;

  public CollectionFilter(CollectionWrapper<Flat> collectionWrapper) {
    loadFullCollection(collectionWrapper);
    changesApplier = new ChangesApplier<>(objectsCollection);
  }

  public void loadFullCollection(CollectionWrapper<Flat> collectionWrapper) {
    objectsCollection = new HashSet<>(collectionWrapper.getCollection());
    creationDateTime = collectionWrapper.getCreationDateTime();
    collectionVersion = collectionWrapper.getVersion();
  }

  public void applyChangelist(CollectionChangelist<Flat> changelist) {
    collectionVersion = changelist.getLatestVersion();
    changesApplier.apply(changelist);
  }

  /**
   * Returns collection creation time
   */
  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  /**
   * Returns collection version. Used in logic clock syncronization.
   */
  public long getCollectionVersion() {
    return collectionVersion;
  }

  /**
   * Returns collection size.
   *
   * @see LinkedHashSet#size()
   */
  public int getSize() {
    return objectsCollection.size();
  }

  /**
   * Returns array of {@link Flat} which name contains passed string.
   *
   * @param filter string to lookup in names.
   */
  public Model[] filterContainsName(String filter) {
    return objectsCollection.stream().sorted().filter(flat -> flat.getName().contains(filter)).toArray(Model[]::new);
  }

  /**
   * Returns a {@link Set} of unique {@link Long} values got by {@link Flat#getNumberOfRooms()} from each entry.
   */
  public Set<Long> getUniqueNumberOfRooms() {
    return objectsCollection.stream().map(Flat::getNumberOfRooms).collect(Collectors.toSet());
  }

  /**
   * Returns an array of {@link Boolean} values in descending order got by {@link Flat#getNewness()} from each entry.
   */
  public Boolean[] getFieldDescendingNew() {
    return objectsCollection.stream().sorted(Comparator.reverseOrder()).map(Flat::getNewness).toArray(Boolean[]::new);
  }
}
