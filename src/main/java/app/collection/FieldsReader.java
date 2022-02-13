package app.collection;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import app.annotations.UserAccess;
import app.exceptions.ReadFailedException;
import app.exceptions.ValueConstraintsException;
import app.exceptions.ValueFormatException;


/**
 * Collects information about data-class fields and ensures that all input values correspond to fields constraints.
 */
public class FieldsReader {
  private final Field[] allFields;
  private final Field[] accessibleFields;

  public FieldsReader(Class<?> dataClass) {
    this.allFields = getRecursiveFields(dataClass);
    this.accessibleFields = getAccessibleFields();
  }

  /**
   * Reads from bufferedReader line by line and validates each entry with corresponding dataClass field.
   * @param reader buffer to read from
   * @param inputMode STORAGE for reading from file/db, INTERACTIVE/SCRIPT - with command prompt
   * @return Array of Objects generated from input strings
   * @throws ReadFailedException when values are invalid or stream is closed
   */
  public Object[] read(BufferedReader reader, FieldsInputMode inputMode) throws ReadFailedException {
    Field[] fields = accessibleFields;
    if (inputMode == FieldsInputMode.STORAGE) {
      fields = allFields;
    }
    Object[] allValues = new Object[fields.length];

    int step = 0;
    while (step < fields.length) {
      Field field = fields[step];

      if (inputMode != FieldsInputMode.STORAGE) {
        printFieldPrompt(field);
      }

      String input;
      try {
        input = reader.readLine();
      } catch (IOException e) {
        throw new ReadFailedException(e.getMessage());
      }

      if (input == null) {
        System.out.println();
        throw new ReadFailedException("No input. Stop reading");
      }
      if (inputMode == FieldsInputMode.SCRIPT) {
        System.out.println(input);
      }

      // check if input value type matches field type and satisfy constraints conditions
      try {
        Object parsedValue = FieldsValidator.parseStrToObject(input, field.getType());
        FieldsValidator.checkConstraints(parsedValue, field);
        allValues[step] = parsedValue;
      } catch (ValueFormatException | ValueConstraintsException e) {
        if (inputMode != FieldsInputMode.STORAGE) {
          System.out.println(e.getMessage());
          continue; // keep staying on this field
        } else {
          throw new ReadFailedException(e.getMessage());
        }
      }
    step++;
  }
  return allValues;
}

  private void printFieldPrompt(Field field) {
      Class<?> fieldClass = field.getType();
      String fieldTypeName = fieldClass.getSimpleName();
      if (fieldClass.isEnum()) {
        fieldTypeName = Stream.of(fieldClass.getEnumConstants())
                              .map(Object::toString)
                              .collect(Collectors.joining(", ", "Enum (", ")"));
      }

      UserAccess elementAnnotation = field.getAnnotation(UserAccess.class);
      System.out.printf("Please enter %s with type %s: ", elementAnnotation.description(), fieldTypeName);
  }

  /**
   * Returns array of fields which are annotated as accessible.
   */
  Field[] getAccessibleFields() {
    return Stream.of(allFields).filter(f -> f.isAnnotationPresent(UserAccess.class)).toArray(Field[]::new);
  }

  /**
   * Recursively get all fields, including those in aggregated classes.
   * @param clazz Data class Type to get fields from
   * @return Array of all fields
   */
  Field[] getRecursiveFields(Class<?> clazz) {
    List<Field> accessibleFields = new ArrayList<>();
    
    for (Field field : clazz.getDeclaredFields()) {
      Class<?> fieldType = field.getType();
      // if field is a custom Type located in same package 
      if (clazz.getPackage().equals(fieldType.getPackage()) && !fieldType.isEnum()) {
        accessibleFields.addAll(Arrays.asList(getRecursiveFields(fieldType)));
        continue;
      }
      accessibleFields.add(field);
    }
    return accessibleFields.toArray(new Field[] {});
  }
}
