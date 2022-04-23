package perform.database.table;

import lombok.Data;
import perform.database.table.column.ColumnDescription;
import perform.util.StringUtil;

import java.util.List;

/**
 * Metaclass which contains table name and columns related to an Entity.
 * Represent one table in Database.
 */
@Data
public class TableDescription {
  private final String tableName;
  private List<ColumnDescription> columns;

  public TableDescription(String tableName) {
    this.tableName = tableName;
  }

  public String getSqlRepresentation() {
    String ls = System.lineSeparator();
    String header = tableName + " (" + ls;
    String lines = StringUtil.join("," + ls, columns);
    String footer = ");";
    return header + lines + footer;
  }
}
