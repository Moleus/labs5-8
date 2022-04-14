package perform.database.table.column;

import lombok.extern.log4j.Log4j2;
import perform.annotations.ForeignKey;
import perform.annotations.GreaterThan;
import perform.annotations.Id;
import perform.mapping.properties.FieldProperty;
import perform.util.PreparedStatementUtil;

import java.lang.annotation.Annotation;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ColumnCreator {
  private final FieldProperty field;
  private final List<Annotation> annotations;

  private final String columnName;
  private final RelationalColumn relationalColumn;

  public ColumnCreator(FieldProperty fieldProperty, String namePrefix) {
    this.field = fieldProperty;
    this.annotations = List.of(fieldProperty.getAnnotations());
    this.columnName = namePrefix + fieldProperty.getColumnName();
    this.relationalColumn = new RelationalColumn(columnName);
  }

  public RelationalColumn createColumn() {
    calculateDataType();
    calculateConstraints();
    return relationalColumn;
  }

  private void calculateDataType() {
    Class<T> fieldType = field.getType();
    this.relationalColumn.setJavaType(fieldType);

    JDBCType sqlDataType = PreparedStatementUtil.getSqlType(fieldType);
    this.relationalColumn.setDataType(sqlDataType);
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
    this.relationalColumn.setConstraints(columnConstraints);
  }

  private Constraint getConstraintType(Annotation annotation) {
    return SqlConstraints.getBy(annotation.annotationType());
  }

  private void fillConstraintParams(Constraint constraint) {
    processAnnotation(field.getAnnotation(GreaterThan.class), constraint);
//    processAnnotation(field.getAnnotation(NotNull.class));
//    processAnnotation(field.getAnnotation(Unique.class));
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

  private void processAnnotation(Id annotation) {
    if (annotation != null) {
      this.relationalColumn.setId(true);
    }
  }

  private void processAnnotation(ForeignKey annotation, Constraint constraint) {
    if (annotation != null) {
      String otherTableName = annotation.tableName();
      constraint.addParameter(otherTableName);
    }
  }
}