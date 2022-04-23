package perform.mapping.table;

import perform.database.table.column.Constraint;

public enum Constraints {
  UNIQUE("UNIQUE"),
  PRIMARY_KEY("PRIMARY KEY"),
  NOT_NULL("NOT NULL"),
  FOREIGN_KEY("REFERENCES"),
  CHECK("CHECK");

  private final String sqlName;

  Constraints(String sqlName) {
    this.sqlName = sqlName;
  }

  public Constraint getConstraint() {
    return Constraint.of(sqlName);
  }
}