package perform.mapping.table;

import perform.annotations.*;
import perform.database.table.column.Constraint;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ClassToSqlMapper {
  private static final Map<Class<? extends Annotation>, Constraints> annotationToSqlConstraint = new HashMap<>();

  static {
    annotationToSqlConstraint.put(Id.class, Constraints.PRIMARY_KEY);
    annotationToSqlConstraint.put(Unique.class, Constraints.UNIQUE);
    annotationToSqlConstraint.put(NotNull.class, Constraints.NOT_NULL);
    annotationToSqlConstraint.put(GreaterThan.class, Constraints.CHECK);
    annotationToSqlConstraint.put(ForeignKey.class, Constraints.FOREIGN_KEY);
  }

  public static Constraint getConstraintType(Class<? extends Annotation> annotation) {
    if (annotationToSqlConstraint.containsKey(annotation)) {
      return annotationToSqlConstraint.get(annotation).getConstraint();
    }
    return null;
  }
}