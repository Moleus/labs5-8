package collection;

import lombok.Getter;
import model.data.Model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

@Getter
public class CollectionChange<T extends Model> implements Serializable {
  private static final long serialVersionUID = 8_0;
  private final Set<T> removed;
  private final Set<T> added;

  CollectionChange(Set<T> removed, Set<T> added) {
    this.removed = removed;
    this.added = added;
  }

  public static <T extends Model> CollectionChange<T> remove(Set<T> removed) {
    return new CollectionChange<>(removed, Collections.emptySet());
  }

  public static <T extends Model> CollectionChange<T> add(Set<T> added) {
    return new CollectionChange<>(Collections.emptySet(), added);
  }

  public static <T extends Model> CollectionChange<T> update(Set<T> updated) {
    return new CollectionChange<>(updated, updated);
  }
}
