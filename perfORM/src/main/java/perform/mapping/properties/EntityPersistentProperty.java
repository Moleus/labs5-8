package perform.mapping.properties;

import org.reflections.Reflections;
import perform.annotations.Embeddable;
import perform.annotations.Table;
import perform.exception.DuplicateKeyException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import static org.reflections.ReflectionUtils.Fields;

public class EntityPersistentProperty<T> implements EntityProperty<T> {
  private final Class<T> entityType;
  private Reflections reflections;
  private final List<FieldProperty<?>> properties = new ArrayList<>();
  private final Map<FieldProperty<?>, EntityProperty<?>> fieldToEmbeddedEntity = new LinkedHashMap<>();

  private FieldProperty<?> idProperty;
  private boolean isEmbeddable;
  private String tableName;

  private EntityPersistentProperty(Class<T> entityType) {
    this.entityType = entityType;
    initializeBeanInfo();
    initProperties();
  }

  public static <U> EntityPersistentProperty<U> of(Class<U> entityType) {
    return new EntityPersistentProperty<>(entityType);
  }

  private void initializeBeanInfo() {
    reflections = new Reflections();
    isEmbeddable = Optional.ofNullable(findAnnotation(Embeddable.class)).isPresent();
    tableName = Optional.ofNullable(findAnnotation(Table.class)).map(Table::name).orElse(entityType.getName());
  }

  private <A extends Annotation> A findAnnotation(Class<A> annotation) {
    return entityType.getAnnotation(annotation);
  }

  private void initProperties() {
    for (Field field : reflections.get(Fields.of(entityType))) {
      FieldProperty<?> property = createFieldProperty(field);
      if (property.isId()) {
        if (idProperty != null) {
          throw new DuplicateKeyException(entityType, "Id");
        }
        idProperty = property;
      }
      if (property.isEmbedded()) {
        fieldToEmbeddedEntity.put(property, EntityPersistentProperty.of(field.getType()));
      }
      properties.add(property);
    }
  }

  private FieldProperty<?> createFieldProperty(Field field) {
    return new FieldPersistentProperty<>(field.getName(), entityType);
  }

  @Override
  public String getName() {
    return entityType.getName();
  }

  @Override
  public Class<T> getType() {
    return entityType;
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  @Override
  public boolean isEmbeddable() {
    return isEmbeddable;
  }

  @Override
  public FieldProperty<?> getIdProperty() {
    return idProperty;
  }

  @Override
  public List<FieldProperty<?>> getProperties() {
    return List.copyOf(properties);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <F> EntityProperty<F> getEmbeddedBy(FieldProperty<F> fieldProperty) {
    return (EntityProperty<F>) fieldToEmbeddedEntity.get(fieldProperty);
  }

  @Override
  public int getColumnsCount() {
    return properties.size();
  }
}