package perform.mapping.properties;

import perform.annotations.Column;
import perform.annotations.Embedded;
import perform.annotations.Id;
import perform.exception.BeanIntrospectionException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public class FieldPersistentProperty<T> implements FieldProperty<T> {
  private final PropertyDescriptor propertyDescriptor;
  private Field field;
  private Annotation[] annotations;

  private final String name;

  private boolean isId;
  private String columnName;

  private boolean isEmbedded;
  private String embeddedPrefix;

  private Method getter;
  private Method setter;

  private Class<T> type;
  private final Class<?> ownerClass;

  public FieldPersistentProperty(String propertyName, Class<?> ownerClass) {
    this.name = propertyName;
    this.ownerClass = ownerClass;
    this.propertyDescriptor = instantiateDescriptor();
    initialize();
  }

  private void initialize() {
    initFieldProp();
    this.isId = Optional.ofNullable(findAnnotation(Id.class)).isPresent();
    this.columnName = Optional.ofNullable(findAnnotation(Column.class)).map(Column::name).orElse(this.name);

    this.isEmbedded = Optional.ofNullable(findAnnotation(Embedded.class)).isPresent();
    this.embeddedPrefix = Optional.ofNullable(findAnnotation(Embedded.class)).map(a -> "".equals(a.prefix()) ? name : a.prefix()).orElse(this.name);

    this.annotations = field.getAnnotations();
    this.getter = propertyDescriptor.getReadMethod();
    this.setter = propertyDescriptor.getWriteMethod();
  }

  private PropertyDescriptor instantiateDescriptor() {
    try {
      return new PropertyDescriptor(name, ownerClass);
    } catch (IntrospectionException e) {
      throw new BeanIntrospectionException(ownerClass, "Introspection failed for persistent property [" + name + "]", e);
    }
  }

  @SuppressWarnings("unchecked")
  private void initFieldProp() {
    try {
      field = ownerClass.getDeclaredField(name);
      type = (Class<T>) propertyDescriptor.getPropertyType();
    } catch (NoSuchFieldException e) {
      throw new BeanIntrospectionException(ownerClass, "Couldn't find a field by name [" + name + "]", e);
    } catch (ClassCastException e) {
      throw new BeanIntrospectionException(ownerClass, "Field name [" + name + "] doesn't match provided Type [" + field.getDeclaringClass() + "]", e);
    }
  }

  private <A extends Annotation> A findAnnotation(Class<A> annotation) {
    return field.getAnnotation(annotation);
  }

  @Override
  public boolean isId() {
    return isId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getColumnName() {
    return columnName;
  }

  @Override
  public Class<T> getType() {
    return type;
  }

  /**
   * Use this to retrieve field constraints when constructing sql table's columns
   */
  @Override
  public Annotation[] getAnnotations() {
    return annotations;
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
    return field.getAnnotation(annotationType);
  }

  @Override
  public boolean isEmbedded() {
    return isEmbedded;
  }

  @Override
  public String getEmbeddedPrefix() {
    return embeddedPrefix;
  }

  @Override
  public Method getGetter() {
    return getter;
  }

  @Override
  public Method getSetter() {
    return setter;
  }
}
