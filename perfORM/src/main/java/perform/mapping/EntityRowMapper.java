package perform.mapping;

import perform.exception.PerformException;
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
  private final Class<T> mappedClass;
  private final EntityClassesRegistry entitiesRegistry;

  private ResultSet resultSet;

  public EntityRowMapper(Class<T> mappedClass, EntityClassesRegistry entitiesRegistry) {
    this.mappedClass = mappedClass;
    this.entitiesRegistry = entitiesRegistry;
  }

  /**
   * Maps each column to an Entity's field.
   * Returns an instance of an Entity with populated fields.
   */
  @Override
  public T mapRow(ResultSet rs) {
    this.resultSet = rs;
    return createEntityFrom(mappedClass);
  }

  private <E> E createEntityFrom(Class<E> clazz) {
    E mappedObject = BeanUtil.instantiateClass(clazz);
    EntityPersistentProperty<E> entityProperty = entitiesRegistry.getEntityProperty(clazz);

    for (FieldProperty property : entityProperty.getProperties()) {
      String setterName = property.getSetter().getName();
      Object value = readFrom(property);
      populateInstance(mappedObject, setterName, value);
    }
    return mappedObject;
  }

  private Object readFrom(FieldProperty property) {
    if (property.isEmbedded()) {
      return createEntityFrom(property.getType());
    }

    try {
      Object value = getPropertyValue(property);
      return JdbcColumnTypes.castUncommonType(value, property.getType());
    } catch (SQLException e) {
      throw new PerformException("Failed to read value from property", e);
    }
  }

  private Object getPropertyValue(FieldProperty property) throws SQLException {
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
