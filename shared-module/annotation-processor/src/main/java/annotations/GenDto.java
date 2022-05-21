package annotations;

import java.lang.annotation.Annotation;

public @interface GenDto {
  Class<? extends Annotation> annotatedWith();
}
