package perform.mapping.mappers;

import perform.exception.MethodInvocationException;
import perform.mapping.properties.EntityProperty;
import perform.mapping.properties.FieldProperty;
import perform.util.PreparedStatementUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class EntityToRowMapper<T> {
  private final EntityProperty<T> entityProperty;

  public EntityToRowMapper(EntityProperty<T> entityProperty) {
    this.entityProperty = entityProperty;
  }

  /**
   * Fills prepared statement with values from an entity.
   *
   * @return next (unfilled) position in prepared statement.
   */
  public synchronized int mapEntity(T entity, PreparedStatement ps) {
    int index = 1;
    return mapEntity(entity, entityProperty, index, ps) + 1;
  }

  @SuppressWarnings("unchecked")
  private <U> int mapEntity(U entity, EntityProperty<U> entityProperty, int psIndex, PreparedStatement ps) {
    List<FieldProperty<?>> properties = entityProperty.getProperties().stream().filter(Predicate.not(FieldProperty::isId)).toList();
    for (FieldProperty<?> property : properties) {
      if (property.isEmbedded()) {
        EntityProperty embeddedProperty = entityProperty.getEmbeddedBy(property);
        psIndex = mapEntity(retrieveValue(entity, property), embeddedProperty, psIndex, ps);
        continue;
      }
      setValue(entity, property, psIndex, ps);
      psIndex++;
    }
    return psIndex;
  }

  private <E, F> void setValue(E entity, FieldProperty<F> property, int psIndex, PreparedStatement ps) {
    Object value = retrieveValue(entity, property);
    try {
      PreparedStatementUtil.setValue(ps, psIndex, value);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private <E, F> Object retrieveValue(E entity, FieldProperty<F> property) {
    Method getter = property.getGetter();
    try {
      return getter.invoke(entity);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new MethodInvocationException(entity.getClass(), getter.getName(), e);
    }
  }
}