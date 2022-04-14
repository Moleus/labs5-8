package perform.mapping.mappers;

import perform.exception.MethodInvocationException;
import perform.mapping.properties.EntityProperty;
import perform.mapping.properties.FieldProperty;
import perform.util.PreparedStatementUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class EntityToRowMapper<T> {
  private final EntityProperty<T> entityProperty;

  private T entity;
  private FieldProperty<?> property;

  public EntityToRowMapper(EntityProperty<T> entityProperty) {
    this.entityProperty = entityProperty;
  }

  /**
   * Fills prepared statement with values from an entity.
   *
   * @return next (unfilled) position in prepared statement.
   */
  public int mapEntity(T entity, PreparedStatement ps) {
    this.entity = entity;
    // create new resultSet or receive it via args.

    // get field getters and types;
    List<FieldProperty<?>> properties = entityProperty.getProperties();
    properties.removeIf(FieldProperty::isId);  // id is generated automatically by db
    for (int i = 1; i < properties.size(); i++) {
      this.property = properties.get(i - 1);
      setField(ps, i);
    }
    return properties.size() + 1;
  }

  private void setField(PreparedStatement ps, int index) {
    Object value = retrieveValue();
    JDBCType jdbcType = PreparedStatementUtil.getSqlType(property.getType());
    try {
      PreparedStatementUtil.setValue(ps, index, jdbcType, value);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private Object retrieveValue() {
    Method getter = property.getGetter();
    try {
      return getter.invoke(entity);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new MethodInvocationException(entity.getClass(), getter.getName(), e);
    }
  }
}