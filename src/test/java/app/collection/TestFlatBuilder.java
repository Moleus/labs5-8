package app.collection;

import static org.junit.jupiter.api.Assertions.*;
import app.collection.data.Coordinates;
import app.collection.data.Flat;
import app.collection.data.House;
import app.collection.data.View;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class TestFlatBuilder {
  final Integer ID = 0;
  final String NAME = "flatName";
  final Integer AREA = 12;
  final Long ROOMS = 12L;
  final boolean HAS_FURNITURE = true;
  final boolean IS_NEW = true;
  final View VIEW = View.TERRIBLE;
  final double COORD_X = 0;
  final Integer COORD_Y = 0;
  final String HOUSE_NAME = "house12";
  final Integer HOUSE_YEAR = 100;
  final Integer HOUSE_FLOORS = 10;
  final Integer HOUSE_LIFTS = 2;
  final LocalDate DATE = LocalDate.now();

  @Test
  @Order(1)
  void testBuildAccessible() {
    final Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    final House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);

    Object[] accessibleParams = {    NAME, COORD_X, COORD_Y,       AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    Flat correctFlat = new  Flat(ID, NAME, coordinates,      DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);

    Flat testFlat = FlatBuilder.getInstance().buildAccessible(accessibleParams);
    assertEquals(correctFlat, testFlat);
  }
  @Test
  @Order(2)
  void testBuildAll() {
    final Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    final House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);

    Object[] allParams =        {ID, NAME, COORD_X, COORD_Y, DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    Flat correctFlat = new  Flat(ID, NAME, coordinates,      DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);

    Flat testFlat = FlatBuilder.getInstance().buildAll(allParams);
    assertEquals(correctFlat, testFlat);
  }
}
