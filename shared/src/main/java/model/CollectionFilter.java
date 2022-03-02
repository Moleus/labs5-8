package model;

import model.data.Flat;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionFilter {
  private final Set<Flat> objectsCollection = new LinkedHashSet<>();
  private LocalDateTime creationDateTime = LocalDateTime.now();

  public void loadCollection(CollectionWrapper wrapper) {
    objectsCollection.addAll(wrapper.getCollection());
    creationDateTime = wrapper.getCreationDateTime();
  }

  /**
   * Returns collection creation time
   */
  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
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
  public Flat[] filterContainsName(String filter) {
    return objectsCollection.stream().sorted().filter(flat -> flat.getName().contains(filter)).toArray(Flat[]::new);
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
