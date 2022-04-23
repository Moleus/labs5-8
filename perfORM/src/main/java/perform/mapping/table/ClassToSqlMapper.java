package perform.mapping.table;

import perform.annotations.*;
import perform.database.table.column.Constraint;
import perform.database.table.column.DataType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ClassToSqlMapper {
  private static final Map<Type, DataTypes> fieldTypeToSqlData = new HashMap<>();
  private static final Map<Class<? extends Annotation>, Constraints> annotationToSqlConstraint = new HashMap<>();

  static {
    fieldTypeToSqlData.put(short.class, DataTypes.INT);
    fieldTypeToSqlData.put(int.class, DataTypes.INT);
    fieldTypeToSqlData.put(long.class, DataTypes.INT);
    fieldTypeToSqlData.put(float.class, DataTypes.INT);
    fieldTypeToSqlData.put(double.class, DataTypes.INT);
    fieldTypeToSqlData.put(Short.class, DataTypes.INT);
    fieldTypeToSqlData.put(Integer.class, DataTypes.INT);
    fieldTypeToSqlData.put(Long.class, DataTypes.INT);
    fieldTypeToSqlData.put(Float.class, DataTypes.INT);
    fieldTypeToSqlData.put(Double.class, DataTypes.INT);
    fieldTypeToSqlData.put(String.class, DataTypes.TEXT);
    fieldTypeToSqlData.put(LocalDateTime.class, DataTypes.TIMESTAMP);
    fieldTypeToSqlData.put(Boolean.class, DataTypes.BOOL);

    annotationToSqlConstraint.put(Id.class, Constraints.PRIMARY_KEY);
    annotationToSqlConstraint.put(Unique.class, Constraints.UNIQUE);
    annotationToSqlConstraint.put(NotNull.class, Constraints.NOT_NULL);
    annotationToSqlConstraint.put(GreaterThan.class, Constraints.CHECK);
    annotationToSqlConstraint.put(ForeignKey.class, Constraints.FOREIGN_KEY);
  }

  public static DataType getDataType(Type fieldType) {
    if (fieldTypeToSqlData.containsKey(fieldType)) {
      return fieldTypeToSqlData.get(fieldType).getDataType();
    }
    return null;
  }

  public static Constraint getConstraintType(Class<? extends Annotation> annotation) {
    if (annotationToSqlConstraint.containsKey(annotation)) {
      return annotationToSqlConstraint.get(annotation).getConstraint();
    }
    return null;
  }
}