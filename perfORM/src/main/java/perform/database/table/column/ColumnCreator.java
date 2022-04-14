package perform.database.table.column;

import lombok.extern.log4j.Log4j2;
import perform.annotations.*;
import perform.mapping.table.ClassToSqlMapper;
import perform.mapping.table.DataTypes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ColumnConstructor {
  private final Field field;
  private final Annotation[] annotations;

  private final String columnName;
  private final ColumnDescription columnDescription;

  public ColumnConstructor(Field annotatedField) {
    this.field = annotatedField;
    this.annotations = annotatedField.getDeclaredAnnotations();
    this.columnName = getColumnName();
    this.columnDescription = new ColumnDescription(columnName);
  }

  public ColumnDescription createColumn() {
    calculateDataType();
    calculateConstraints();
    return columnDescription;
  }

  private String getColumnName() {
    Column annotation = field.getAnnotation(Column.class);
    String columnName = annotation == null ? field.getName() : annotation.name();
    if (columnName.isEmpty()) throw new IllegalArgumentException("Column name can't be empty.");
    return columnName;
  }

  private void calculateDataType() {
    DataType sqlDataType = ClassToSqlMapper.getDataType(field.getType());
    this.columnDescription.setDataType(sqlDataType);
  }

  private void calculateConstraints() {
    List<Constraint> columnConstraints = new ArrayList<>();
    for (Annotation annotation : annotations) {
      Constraint constraint = getConstraintType(annotation);
      if (constraint == null) {
        log.debug("Annotation {} is not a constraint", annotation);
        continue;
      }
      fillConstraintParams(constraint);
      columnConstraints.add(constraint);
    }
    this.columnDescription.setConstraints(columnConstraints);
  }

  private Constraint getConstraintType(Annotation annotation) {
    return ClassToSqlMapper.getConstraintType(annotation.annotationType());
  }

  private void fillConstraintParams(Constraint constraint) {
    processAnnotation(field.getAnnotation(GreaterThan.class), constraint);
    processAnnotation(field.getAnnotation(NotNull.class));
    processAnnotation(field.getAnnotation(Unique.class));
    processAnnotation(field.getAnnotation(Id.class));
    processAnnotation(field.getAnnotation(ForeignKey.class), constraint);
  }

  private void processAnnotation(GreaterThan annotation, Constraint constraint) {
    // TODO: check that field type is numeric
    if (annotation != null) {
      String expression = columnName + " > " + annotation.num();
      constraint.addExpression(expression);
    }
  }

  private void processAnnotation(NotNull annotation) {
    if (annotation != null) {
      // No additional parameters or expression needed for NOT NULL constraint.
    }
  }

  private void processAnnotation(Unique annotation) {
    if (annotation != null) {
      // No additional parameters or expression needed for UNIQUE constraint.
    }
  }

  private void processAnnotation(Id annotation) {
    if (annotation != null) {
      this.columnDescription.setDataType(DataTypes.SERIAL.getDataType());
    }
  }

  private void processAnnotation(ForeignKey annotation, Constraint constraint) {
    if (annotation != null) {
      String otherTableName = annotation.tableName();
      constraint.addParameter(otherTableName);
    }
  }
}