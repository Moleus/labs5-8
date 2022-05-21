package perform.util;

import perform.exception.FieldNotFoundException;
import perform.exception.InvalidRepositoryException;
import perform.exception.PerformException;
import perform.mapping.properties.EntityProperty;
import perform.mapping.properties.FieldProperty;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodUtil {
  /**
   * package-private - for tests only
   */
  static final String FIND_BY_PREFIX = "findBy";

  public static boolean isFind(Method method) {
    return method.getName().startsWith(FIND_BY_PREFIX);
  }

  public static void checkFindMethod(EntityProperty<?> entity, Method method) {
    assert isFind(method);
    String suffix = getFindSuffix(method.getName()).toLowerCase();
    Class<?> fieldType = getFieldByName(entity, suffix);
    Class<?> parameterType = getParameterType(method);
    if (!fieldType.equals(parameterType)) {
      throw new InvalidRepositoryException("Type of field [" + suffix + "] doesn't match parameter type [" + parameterType + "]");
    }
  }

  public static Class<?> getFieldByName(EntityProperty<?> entityProperty, String name) {
    return entityProperty.getProperties().stream()
        .filter(field -> field.getName().equals(name))
        .findFirst()
        .map(FieldProperty::getType)
        .orElseThrow(() -> new FieldNotFoundException(name, entityProperty.getType()));
  }

  public static Class<?> getParameterType(Method method) {
    Parameter param = getSingleParam(method);
    return param.getType();
  }

  private static Parameter getSingleParam(Method method) {
    Parameter[] parameters = method.getParameters();
    if (parameters.length != 1) {
      throw new PerformException("Expected only one parameter passed to method [" + method.getName() + "]");
    }
    return parameters[0];
  }

  public static String getFindSuffix(String name) {
    return name.replace(FIND_BY_PREFIX, "");
  }
}
