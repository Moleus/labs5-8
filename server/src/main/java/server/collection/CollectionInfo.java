package server.collection;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectionInfo {
  private final LocalDateTime creationDateTime;
  private final long version;
}
