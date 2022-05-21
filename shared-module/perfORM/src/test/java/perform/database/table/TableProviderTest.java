package perform.database.table;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import perform.annotations.Id;
import perform.exception.DuplicateKeyException;
import perform.mapping.properties.EntityPersistentProperty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableProviderTest {
  @Test
  public void getTable_ShouldThrowException_WhenDuplicatedId() {
    Exception exception = assertThrows(DuplicateKeyException.class, () -> EntityPersistentProperty.of(DuplicatesEntity.class));

    String expectedMessage = "Found duplicated [Id]";
    String actualMessage = exception.getMessage();
    assertThat(actualMessage).contains(expectedMessage);
  }

  @Getter
  @Setter
  private static class DuplicatesEntity {
    @Id
    int id1;
    @Id
    int id2;
  }
}
