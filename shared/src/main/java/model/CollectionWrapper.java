package model;

import lombok.Data;
import model.data.Flat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data(staticConstructor = "of")
public class CollectionWrapper implements Serializable {
  private final Set<Flat> collection;
  private final LocalDateTime creationDateTime;
}
