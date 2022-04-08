package collection;

import lombok.Data;
import model.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data(staticConstructor = "of")
public class CollectionWrapper implements Serializable {
  private final Set<Model> collection;
  private final LocalDateTime creationDateTime;
  private final long version;
}
