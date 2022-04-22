package perform.mapping.properties;

import perform.annotations.Embeddable;
import perform.annotations.Table;
import perform.exception.BeanIntrospectionException;
import perform.exception.DuplicateKeyException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.*;

public class EntityPersistentProperty<T> implements EntityProperty<T> {
  private final Class<T> entityType;
  private BeanInfo beanInfo;
  private final List<FieldProperty<?>> properties = new ArrayList<>();
  private final Map<FieldProperty<?>, EntityProperty<?>> fieldToEmbeddedEntity = new HashMap<>();

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
    try {
      beanInfo = Introspector.getBeanInfo(entityType);
      isEmbeddable = Optional.ofNullable(findAnnotation(Embeddable.class)).isPresent();
      tableName = Optional.ofNullable(findAnnotation(Table.class)).map(Table::name).orElse(entityType.getName());
    } catch (IntrospectionException e) {
      throw new BeanIntrospectionException(entityType, "Initialization of persistent entity failed", e);
    }
  }

  private <A extends Annotation> A findAnnotation(Class<A> annotation) {
    return entityType.getAnnotation(annotation);
  }

  private void initProperties() {
    for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
      Class<?> fieldType = descriptor.getPropertyType();
      if (Class.class.equals(fieldType)) {
        continue;  // skip first 'Class' property
      }
      FieldProperty<?> property = new FieldPersistentProperty<>(descriptor, entityType);
      if (property.isId()) {
        if (idProperty != null) {
          throw new DuplicateKeyException(entityType, "Id");
        }
        idProperty = property;
      }
      if (property.isEmbedded()) {
        fieldToEmbeddedEntity.put(property, EntityPersistentProperty.of(fieldType));
      }
      properties.add(property);
    }
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