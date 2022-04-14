package perform.mapping.properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface FieldProperty extends Property {
  boolean isId();

  String getColumnName();

  Annotation[] getAnnotations();

  <A extends Annotation> A getAnnotation(Class<A> annotationType);

  boolean isEmbedded();

  String getEmbeddedPrefix();

  Method getGetter();

  Method getSetter();
}
