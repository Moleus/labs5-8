package perform.mapping.mappers;

import java.sql.ResultSet;

public interface RowMapper<T> {
  T mapRow(ResultSet rs);
  // Reading EmbeddedEntity we need idValue
  // When reading value or Entity we need only Property
}
