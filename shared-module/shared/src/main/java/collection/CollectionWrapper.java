package collection;

import lombok.Data;
import model.data.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data(staticConstructor = "of")
public class CollectionWrapper<T extends Model> implements Serializable {
  private static final long serialVersionUID = 8_0;
  private final Set<T> collection;
  private final LocalDateTime creationDateTime;
  private final long version;
}
