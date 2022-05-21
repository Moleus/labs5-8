package perform.database.table.column;

import lombok.Data;
import perform.util.PreparedStatementUtil;
import perform.util.StringUtil;

import java.sql.JDBCType;
import java.util.List;

/**
 * Contains type information and constraints related to an Entity field.
 * Represents one column in Database table.
 */
@Data
public class RelationalColumn<T> {
  private final String columnName;
  private JDBCType dataType;
  private Class<T> javaType;
  private boolean isId = false;
  private String foreignRelation;
  private List<Constraint> constraints;

  @Override
  public String toString() {
    String sqlConstraints = StringUtil.join(" ", constraints);
    String sqlType = isId ? "BIGSERIAL" : PreparedStatementUtil.getPgsqlName(dataType);
    return StringUtil.join(" ", columnName, sqlType, sqlConstraints);
  }
}
