package app.collection;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import app.annotations.GreaterThan;
import app.annotations.NotNull;
import app.exceptions.ValueConstraintsException;
import app.exceptions.ValueFormatException;


class FieldsValidator {
  /**
   * @param value User input or value from file coverted to object
   * @param field Field corresponding to input
   * @throws ValueConstraintsException If the input doesn't satisfy field's restrictions, such as 'greater than' or 'not null'.
   */
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
      if ( value == null || ((Number) value).doubleValue() <= gtValue) {
        throw new ValueConstraintsException(String.format("Value '%s' should be greater than '%s'", fieldName, gtValue));
      }
    }
  }

  /**
   * @return New correct type object. Null if string is empty.
   * @throws ValueFormatException  if {@code str} can't be converted to {@code fieldClass}
   */
  public static Object parseStrToObject(String str, Class<?> fieldClass) throws ValueFormatException {
    PropertyEditor editor;
    if (str.trim().equals("")) return null;
    try {
      if (fieldClass == LocalDate.class) {
        return LocalDate.parse(str);
      }
      editor = PropertyEditorManager.findEditor(fieldClass);
      if (null == editor) {
        throw new RuntimeException("No suitable converter found for type: " + fieldClass.getSimpleName());
      }
      editor.setAsText(str);
    } catch (IllegalArgumentException | DateTimeParseException e){
      throw new ValueFormatException("Failed to convert value to type " + fieldClass.getSimpleName());
    }
    return editor.getValue();
  }
}