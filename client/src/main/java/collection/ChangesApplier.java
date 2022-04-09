package collection;

import model.data.Model;

import java.util.Set;

public class ChangesApplier<T extends Model> {
  private final Set<T> target;

  public ChangesApplier(Set<T> target) {
    this.target = target;
  }

  public void apply(CollectionChangelist<T> changelist) {
    for (CollectionChange<T> change : changelist.getChangelist().values()) {
      applyChange(change);
    }
  }

  private void applyChange(CollectionChange<T> change) {
    target.removeAll(change.getRemoved());
    target.addAll(change.getAdded());
  }
}
