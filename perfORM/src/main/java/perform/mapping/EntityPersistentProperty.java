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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityPersistentProperty<T> implements EntityProperty {
  private final Class<T> entityType;
  private BeanInfo beanInfo;
  private final List<FieldProperty> properties = new ArrayList<>();

  private FieldProperty idProperty;
  private boolean isEmbeddable;
  private String tableName;

  public EntityPersistentProperty(Class<T> entityType) {
    this.entityType = entityType;
    initializeBeanInfo();
    initProperties();
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
      FieldProperty property = new FieldPersistentProperty(descriptor, entityType);
      if (property.isId()) {
        if (idProperty != null) {
          throw new DuplicateKeyException(entityType, "Id");
        }
        idProperty = property;
      }
      properties.add(property);
    }
  }

  @Override
  public String getName() {
    return entityType.getName();
  }

  @Override
  public Class<?> getType() {
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
  public FieldProperty getIdProperty() {
    return idProperty;
  }

  @Override
  public List<FieldProperty> getProperties() {
    return List.copyOf(properties);
  }

  @Override
  public int getColumnsCount() {
    return properties.size();
  }
}