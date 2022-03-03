package collection;

import lombok.Data;
import model.data.Flat;

import java.io.Serializable;
import java.util.Set;

@Data(staticConstructor = "of")
public
class CollectionChange implements Serializable {
  private final Set<Flat> removed;
  private final Set<Flat> added;
}
