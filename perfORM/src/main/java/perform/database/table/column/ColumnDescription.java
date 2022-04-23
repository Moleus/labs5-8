package perform.database.table.column;

import lombok.Data;
import perform.util.StringUtil;

import java.util.List;

/**
 * Contains type information and constraints related to an Entity field.
 * Represents one column in Database table.
 */
@Data
public class ColumnDescription {
  private final String columnName;
  private DataType dataType;
  private List<Constraint> constraints;

  @Override
  public String toString() {
    String sqlConstraints = StringUtil.join(" ", constraints);
    return StringUtil.join(" ", columnName, dataType.toString(), sqlConstraints);
  }
}