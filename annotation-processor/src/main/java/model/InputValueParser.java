package model;

import exceptions.ValueFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.function.Function;


public class InputValueParser {
  private static final HashMap<Class<?>, Function<String, ?>> parser = new HashMap<>();

  static {
    parser.put(int.class, Integer::parseInt);
    parser.put(long.class, Long::parseLong);
    parser.put(double.class, Double::valueOf);
    parser.put(Integer.class, Integer::valueOf);
    parser.put(Long.class, Long::valueOf);
    parser.put(Double.class, Double::valueOf);
    parser.put(Float.class, Float::valueOf);
    parser.put(String.class, String::valueOf);
    parser.put(LocalDate.class, LocalDate::parse);
  }

  private final String userInput;
  private final Class<?> fieldClass;
  private final String errorMessage;

  public InputValueParser(String userInput, Class<?> fieldClass) {
    this.userInput = userInput.trim();
    this.fieldClass = fieldClass;
    this.errorMessage = "Failed while parsing field " + fieldClass.getSimpleName();
  }

  /**
   * @return New correct type object. Null if string is empty.
   * @throws ValueFormatException if {@code str} can't be converted to {@code fieldClass}
   */
  public Object parse() throws ValueFormatException {
    if (userInput.length() == 0 && !fieldClass.isPrimitive()) return null;

    Function<String, ?> func = parser.get(fieldClass);
    if (func != null) {
      return convertByFunction(func);
    }
    if (fieldClass.isEnum()) {
      return parseEnum();
    }
    if (fieldClass == Boolean.class || fieldClass == boolean.class) {
      return parseBoolean();
    }
    throw new UnsupportedOperationException("Can't parse string to " + fieldClass.getName());
  }

  private Object convertByFunction(Function<String, ?> func) throws ValueFormatException {
    try {
      return func.apply(userInput);
    } catch (NumberFormatException | DateTimeParseException e) {
      throw new ValueFormatException(errorMessage);
    }
  }

  private Object parseEnum() throws ValueFormatException {
    try {
      @SuppressWarnings({"unchecked", "rawtypes"})
      Object enumConstant = Enum.valueOf((Class<Enum>) fieldClass, userInput);
      return enumConstant;
    } catch (IllegalArgumentException e) {
      throw new ValueFormatException(errorMessage);
    }
  }

  private Object parseBoolean() throws ValueFormatException {
    return switch (userInput) {
      case "true" -> Boolean.TRUE;
      case "false" -> Boolean.FALSE;
      default -> throw new ValueFormatException(errorMessage);
    };
  }
}