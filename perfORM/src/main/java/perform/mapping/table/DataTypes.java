package perform.mapping.table;

import perform.database.table.column.DataType;

public enum DataTypes {
  INT("INT"),
  TIMESTAMP("TIMESTAMP"),
  TEXT("TEXT"),
  BOOL("BOOL"),
  SERIAL("SERIAL");

  private final String sqlName;

  DataTypes(String sqlName) {
    this.sqlName = sqlName;
  }

  public DataType getDataType() {
    return DataType.of(sqlName);
  }
}
