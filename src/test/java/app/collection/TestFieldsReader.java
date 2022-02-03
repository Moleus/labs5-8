package app.collection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import app.collection.data.Coordinates;
import app.collection.data.Flat;
import app.collection.data.House;
import app.collection.data.View;
import app.exceptions.ReadFailedException;

public class TestFieldsReader {
  private BufferedReader reader;
  private Field[] correctAccFields;
  private FieldsReader fieldReader;
  // паттерн для тестов такой: {methodName}_Should{do}_When{Condition}.


  TestFieldsReader(){
    prepareReader(System.in);
  }

  void prepareReader(InputStream stream) {
    this.reader = new BufferedReader(new InputStreamReader(stream));
    this.fieldReader = new FieldsReader(Flat.class);
  }

  InputStream getStream(String str) {
    return new ByteArrayInputStream(str.getBytes());
  }

  @Test
  void readAllInteractiveTest() throws ReadFailedException {
    Object[] correctReadResult = new Object[] { "flatName", (double)0, 0, 12, 100L, Boolean.TRUE, Boolean.FALSE, View.TERRIBLE, "houseName", 1999, 9, 2};

    String testString = Stream.of(correctReadResult).map(Object::toString).collect(Collectors.joining("\n", "", "\n"));

    prepareReader(getStream(testString));
    Object[] readResult = fieldReader.read(reader, FieldsInputMode.INTERACTIVE);
    assertArrayEquals(readResult, correctReadResult);
  }

  @Test
  void readAllStorageTest() throws ReadFailedException {
    Object[] correctReadResult = new Object[] { 12, "flatName", (double)0, 0, LocalDate.parse("1212-12-12"), 12, 100L, Boolean.TRUE, Boolean.FALSE, View.TERRIBLE, "houseName", 1999, 9, 2};

    String testString = Stream.of(correctReadResult).map(Object::toString).collect(Collectors.joining("\n", "", "\n"));
    prepareReader(getStream(testString));

    Object[] readResult = fieldReader.read(reader, FieldsInputMode.STORAGE);
    assertArrayEquals(readResult, correctReadResult);
  }


  @Test
  void accessibleFieldsNamesTest() {
    Field[] accFields = fieldReader.getAccessibleFields();
    String[] fieldNames = Arrays.stream(accFields).map(Field::getName).toArray(String[]::new);
    String[] correctNames = getCorrectAccessibleFieldNames();
    assertArrayEquals(fieldNames, correctNames);
  }

  @Test
  void allFieldsNamesTest() {
    Field[] allFields = fieldReader.getRecursiveFields(Flat.class);
    String[] fieldNames = Arrays.stream(allFields).map(Field::getName).toArray(String[]::new);
    String[] correctNames = getCorrectAllFieldNames();
    assertArrayEquals(fieldNames, correctNames);
  }

  void prepareFlat() {
    Coordinates coordinates = new Coordinates(0, 0);
    House house = new House("name", 1, 2, 2);
    Flat flatObj = FlatBuilder.getInstance().build(1, "name", coordinates, LocalDate.now(),
        10, 1L, true, true, View.GOOD, house);
  }


  String[] getCorrectAccessibleFieldNames() {
    return new String[]{ "name", "x", "y", "area", "numberOfRooms", "furniture", "newness", "view", "name", "year", "numberOfFloors", "numberOfLifts" };
  }
  String[] getCorrectAllFieldNames() {
    return new String[] {"id", "name", "x", "y", "creationDate", "area", "numberOfRooms", "furniture", "newness", "view", "name", "year", "numberOfFloors", "numberOfLifts" };
  }
}
