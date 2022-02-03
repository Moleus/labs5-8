package app.collection;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import app.annotations.GreaterThan;
import app.annotations.NotNull;
import app.exceptions.ValueConstraintsException;
import app.exceptions.ValueFormatException;


class FieldsValidator {
  public static void checkConstraints(Object value, Field field) throws ValueConstraintsException {
    String fieldName = field.getName();
    Set<Class<?>> numericPrimitives = Stream.of(double.class, int.class, long.class, float.class).collect(Collectors.toSet());

    if (field.isAnnotationPresent(NotNull.class) && value == null) {
      throw new ValueConstraintsException(String.format("Value '%s' can't be Null!", fieldName));
    }

    // handle greaterThan annotation
    if (field.isAnnotationPresent(GreaterThan.class)) {
      if (!Number.class.isAssignableFrom(field.getType()) && !numericPrimitives.contains(field.getType())) {
        throw new RuntimeException("Remove @GreaterThan annotation from nonnumerical field '" + fieldName + "'");
      }
      double gtValue = field.getAnnotation(GreaterThan.class).num();
      if (((Number) value).doubleValue() <= gtValue) {
        throw new ValueConstraintsException(String.format("Value '%s' should be greater than '%s'", fieldName, gtValue));
      }
    }
  }

  /**
   * Returns nothing if the input is correct.
   * @param input User input or value from file to validate
   * @param field Field corresponding to input
   * @throws ValueConstraintsException If the input doesn't satisfy field's restrictions, such as 'greater than' or 'not null'. 
   * @throws ValueFormatException If the input can't be converted to a field's Type.
   */
  public static Object parseValue(String input, Field field) throws ValueConstraintsException, ValueFormatException {
    Object parsedValue = parseStrToObject(input, field.getType());
    checkConstraints(parsedValue, field);
    return parsedValue;
  }

  // https://stackoverflow.com/questions/36368235/java-get-valueof-for-generic-subclass-of-java-lang-number-or-primitive
  /**
   * @return New correct type object. Null if string is empty.
   * @throws ValueFormatException  if {@code str} can't be converted to {@code fieldClass}
   */
  public static Object parseStrToObject(String str, Class<?> fieldClass) throws ValueFormatException {
    HashMap<Class<?>, Function<String,?>> parser = new HashMap<>();
    parser.put(int.class    , Integer::parseInt);
    parser.put(long.class   , Long::parseLong);
    parser.put(double.class   , Double::valueOf);
    parser.put(Integer.class, Integer::valueOf);
    parser.put(Long.class   , Long::valueOf);
    parser.put(Double.class , Double::valueOf);
    parser.put(Float.class  , Float::valueOf);
    parser.put(String.class , String::valueOf);
    parser.put(LocalDate.class , LocalDate::parse);
    String errorMessage = "Failed while parsing field " + fieldClass.getSimpleName();

    if (str.length() == 0) return null;

    Function<String,?> func = parser.get(fieldClass);
    if (func != null) {
      try {
        return func.apply(str);
      } catch ( NumberFormatException | DateTimeParseException e) {
        throw new ValueFormatException(errorMessage);
      }
    }
    
    if (fieldClass.isEnum())
      try {
        @SuppressWarnings({"unchecked", "rawtypes"})
        Object enumConstant = Enum.valueOf((Class<Enum>) fieldClass, str);
        return enumConstant;
      } catch (IllegalArgumentException e) {
        throw new ValueFormatException(errorMessage);
      }
    if (fieldClass == Boolean.class || fieldClass == boolean.class) {
      if ("true".equals(str)) return Boolean.TRUE;
      if ("false".equals(str)) return Boolean.FALSE;
      throw new ValueFormatException(errorMessage);
    } 

    throw new UnsupportedOperationException("Can't parse string to " + fieldClass.getName());
  }
}