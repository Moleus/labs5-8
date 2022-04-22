package perform.mapping.properties;

import java.util.List;

public interface EntityProperty<T> extends Property<T> {
  String getTableName();

  boolean isEmbeddable();

  FieldProperty<?> getIdProperty();

  List<FieldProperty> getProperties();

  int getColumnsCount();
}
