package perform.mapping.mappers;

import perform.exception.PerformException;
import perform.mapping.properties.EntityProperty;
import perform.mapping.properties.FieldProperty;
import perform.util.BeanUtil;
import perform.util.JdbcColumnTypes;

import java.beans.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a result row to an Entity object.
 * Initializes all embedded classes under the hood.
 *
 * @param <T> entity type
 */
public class EntityRowMapper<T> implements RowMapper<T> {
  private final EntityProperty<T> entityProperty;

  private ResultSet resultSet;

  public EntityRowMapper(EntityProperty<T> entityProperty) {
    this.entityProperty = entityProperty;
  }

  /**
   * Maps each column to an Entity's field.
   * Returns an instance of an Entity with populated fields.
   */
  @Override
  public T mapRow(ResultSet rs) {
    this.resultSet = rs;
    return createEntityFrom(entityProperty);
  }

  private <E> E createEntityFrom(EntityProperty<E> entityProperty) {
    E mappedObject = BeanUtil.instantiateClass(entityProperty.getType());

    for (FieldProperty<?> property : entityProperty.getProperties()) {
      String setterName = property.getSetter().getName();
      Object value = readFrom(property);
      populateInstance(mappedObject, setterName, value);
    }
    return mappedObject;
  }

  private <F> F readFrom(FieldProperty<F> property) {
    if (property.isEmbedded()) {
      // Because we call the method from a root Entity it allows only 2 levels of embedding.
      return createEntityFrom(entityProperty.getEmbeddedBy(property));
    }

    try {
      Object value = getPropertyValue(property);
      // At this moment there is only one type that differs from table representation:
      //    Enum is stored as String in table, so must be converted here to correct type.
      return JdbcColumnTypes.castUncommonType(value, property.getType());
    } catch (SQLException e) {
      throw new PerformException("Failed to read value from property", e);
    }
  }

  private Object getPropertyValue(FieldProperty<?> property) throws SQLException {
    Class<?> jdbcType = JdbcColumnTypes.INSTANCE.resolvePrimitiveType(property.getType());
    return resultSet.getObject(property.getColumnName(), jdbcType);
  }

  private void populateInstance(Object instance, String setterName, Object value) {
    Statement statement = new Statement(instance, setterName, new Object[]{value});
    try {
      statement.execute();
    } catch (Exception e) {
      throw new PerformException("Failed to call setter [" + setterName + "] on [" + instance.getClass().getName() + "]", e);
    }
  }
}