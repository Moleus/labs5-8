package collection;

import lombok.Data;
import model.Model;

import java.io.Serializable;
import java.util.Set;

@Data(staticConstructor = "of")
public
class CollectionChange implements Serializable {
  private final Set<Model> removed;
  private final Set<Model> added;
}
