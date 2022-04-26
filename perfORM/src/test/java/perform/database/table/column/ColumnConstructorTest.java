package perform.database.table.column;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import perform.annotations.Column;
import perform.annotations.GreaterThan;
import perform.annotations.Id;
import perform.annotations.NotNull;
import perform.mapping.properties.FieldPersistentProperty;
import perform.mapping.properties.FieldProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColumnConstructorTest {

  //  <method>_Should<expected>_When<condition>
  @Test
  public void createColumn_ShouldGenConstraints_WhenGreaterThan() {
    FieldProperty<TestingEntity> lengthProp = new FieldPersistentProperty<>("length", TestingEntity.class);
    FieldProperty<TestingEntity> speedProp = new FieldPersistentProperty<>("speed", TestingEntity.class);
    RelationalColumn<TestingEntity> lengthColumn = (new ColumnCreator<>(lengthProp, "")).createColumn();
    RelationalColumn<TestingEntity> speedColumn = (new ColumnCreator<>(speedProp, "")).createColumn();

    assertEquals("length FLOAT CHECK (length > 0.0)", lengthColumn.toString());
    assertEquals("speed FLOAT CHECK (speed > -1.1)", speedColumn.toString());
  }

  @Test
  public void createColumn_ShouldChangeColName_WhenAnnotated() {
    FieldProperty<TestingEntity> idProp = new FieldPersistentProperty<>("id", TestingEntity.class);
    FieldProperty<TestingEntity> numberProp = new FieldPersistentProperty<>("number", TestingEntity.class);

    RelationalColumn<TestingEntity> idColumn = (new ColumnCreator<>(idProp, "")).createColumn();
    assertEquals("spec_id", idColumn.getColumnName());
  }

  @Test
  public void createColumn_ShouldGenIdAndNotNull_WhenAnnotated() {
    FieldProperty<TestingEntity> idProp = new FieldPersistentProperty<>("id3", TestingEntity.class);

    RelationalColumn<TestingEntity> idColumn = (new ColumnCreator<>(idProp, "")).createColumn();
    assertEquals("id3 BIGSERIAL PRIMARY KEY", idColumn.toString());
  }

  @Getter
  @Setter
  private static class TestingEntity {
    @GreaterThan
    double length;
    @GreaterThan(num = -1.1)
    double speed;

    @Column(name = "spec_id")
    int id;
    @Column(name = "")
    long number;

    @Id
    @NotNull
    int id3;
  }
}