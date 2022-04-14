package perform.database.table.column;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.CaseUtils;
import perform.annotations.ForeignKey;
import perform.annotations.GreaterThan;
import perform.annotations.Id;
import perform.mapping.properties.FieldProperty;
import perform.mapping.table.Constraints;
import perform.util.PreparedStatementUtil;

import java.lang.annotation.Annotation;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ColumnCreator<T> {
  private final FieldProperty<T> field;
  private final List<Annotation> annotations;

  private final String columnName;
  private final RelationalColumn<T> relationalColumn;

  public ColumnCreator(FieldProperty<T> fieldProperty, String namePrefix) {
    this.field = fieldProperty;
    this.annotations = List.of(fieldProperty.getAnnotations());
    this.columnName = CaseUtils.toCamelCase(namePrefix + " " + fieldProperty.getColumnName(), false);
    this.relationalColumn = new RelationalColumn<>(columnName);
  }

  public RelationalColumn<T> createColumn() {
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
      processAnnotations(annotation, constraint);
      columnConstraints.add(constraint);
    }
    if (relationalColumn.isId()) {
      // Remove other constraints
      columnConstraints.removeIf(c -> !(c.getName().equals(Constraints.PRIMARY_KEY.getConstraint().getName())));
    }
    this.relationalColumn.setConstraints(columnConstraints);
  }

  private Constraint getConstraintType(Annotation annotation) {
    return SqlConstraints.getBy(annotation.annotationType());
  }

  private void processAnnotations(Annotation annotation, Constraint constraint) {
    // TODO: check that field type is numeric
    if (annotation instanceof GreaterThan greaterThan) {
      String expression = columnName + " > " + greaterThan.num();
      constraint.addExpression(expression);
    }
    if (annotation instanceof Id) {
      this.relationalColumn.setId(true);
    }
    if (annotation instanceof ForeignKey foreignKey) {
      String otherTableName = foreignKey.tableName();
      constraint.addParameter(otherTableName);
    }
  }
}
