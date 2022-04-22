package perform.database.query;

import perform.database.table.RelationalTable;
import perform.database.table.column.RelationalColumn;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Statements {
  private final String SELECT_ALL;
  private final String SELECT_BY_TEMPLATE;
  private final String DELETE_ALL;
  private final String DELETE_BY_TEMPLATE;
  private final String UPDATE_TEMPLATE;
  private final String INSERT;

  private final List<String> columnNames;

  public Statements(RelationalTable table) {
    String tableName = table.getTableName();
    columnNames = table.getColumns().stream()
        .filter(Predicate.not(RelationalColumn::isId))
        .map(RelationalColumn::getColumnName).toList();

    this.SELECT_ALL = "SELECT * FROM " + tableName;
    this.SELECT_BY_TEMPLATE = SELECT_ALL + " WHERE %s = ?";

    this.DELETE_ALL = "DELETE * FROM " + tableName;
    this.DELETE_BY_TEMPLATE = DELETE_ALL + " WHERE %s = ?";

    String commaSepNames = String.join(", ", columnNames);
    String commaSepQMarks = String.join(", ", Collections.nCopies(columnNames.size(), "?"));
    this.INSERT = "INSERT INTO " + tableName + " " + commaSepNames + " VALUES (" + commaSepQMarks + ")";
    this.UPDATE_TEMPLATE = "UPDATE " + tableName + " SET (" + commaSepNames + ") = (" + commaSepQMarks + ") WHERE %s = ?";
  }

  public String selectAll() {
    return SELECT_ALL;
  }

  public String selectBy(String columnName) {
    assert columnNames.contains(columnName);
    return String.format(SELECT_BY_TEMPLATE, columnName);
  }

  public String deleteAll() {
    return DELETE_ALL;
  }

  public String deleteBy(String columnName) {
    assert columnNames.contains(columnName);
    return String.format(DELETE_BY_TEMPLATE, columnName);
  }

  public String insert() {
    return INSERT;
  }

  public String updateBy(String columnName) {
    assert columnNames.contains(columnName);
    return String.format(UPDATE_TEMPLATE, columnName);
  }
}