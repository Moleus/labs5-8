package collection;

import model.data.Flat;

import java.util.Set;

public class ChangesApplier {
  private final Set<Flat> target;

  public ChangesApplier(Set<Flat> target) {
    this.target = target;
  }

  public void apply(CollectionChangelist changelist) {
    for (CollectionChange change : changelist.getChangelist().values()) {
      applyChange(change);
    }
  }

  private void applyChange(CollectionChange change) {
    target.removeAll(change.getRemoved());
    target.addAll(change.getAdded());
  }
}
