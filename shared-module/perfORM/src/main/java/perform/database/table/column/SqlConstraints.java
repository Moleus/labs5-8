package perform.database.table.column;

import perform.annotations.*;
import perform.mapping.table.Constraints;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class SqlConstraints {
  private static final Map<Class<? extends Annotation>, Constraints> annotationToSqlConstraint = new HashMap<>();

  static {
    annotationToSqlConstraint.put(Id.class, Constraints.PRIMARY_KEY);
    annotationToSqlConstraint.put(Unique.class, Constraints.UNIQUE);
    annotationToSqlConstraint.put(NotNull.class, Constraints.NOT_NULL);
    annotationToSqlConstraint.put(GreaterThan.class, Constraints.CHECK);
    annotationToSqlConstraint.put(ForeignKey.class, Constraints.FOREIGN_KEY);
  }

  public static Constraint getBy(Class<? extends Annotation> annotation) {
    if (annotationToSqlConstraint.containsKey(annotation)) {
      return annotationToSqlConstraint.get(annotation).getConstraint();
    }
    return null;
  }
}