package app.collection;

import app.collection.data.Coordinates;
import app.collection.data.Flat;
import app.collection.data.House;
import app.exceptions.InvalidDataValues;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static util.FlatValues.*;

public class TestFlatBuilder {

  @Test
  @Order(1)
  void testBuildAccessible() throws InvalidDataValues {
    final Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    final House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);

    Object[] accessibleParams = {    NAME, COORD_X, COORD_Y,       AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    Flat correctFlat = new  Flat(ID, NAME, coordinates,      DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);

    Flat testFlat = FlatBuilder.createInstance().buildAccessible(accessibleParams);
    assertEquals(correctFlat, testFlat);
  }
  @Test
  @Order(2)
  void testBuildAll() throws InvalidDataValues {
    final Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    final House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);

    Object[] allParams =        {ID, NAME, COORD_X, COORD_Y, DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    Flat correctFlat = new  Flat(ID, NAME, coordinates,      DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);

    Flat testFlat = FlatBuilder.createInstance().buildAll(allParams);
    assertEquals(correctFlat, testFlat);
  }
}
