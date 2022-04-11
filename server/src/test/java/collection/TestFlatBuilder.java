package collection;

import model.data.Coordinates;
import model.data.Flat;
import model.data.House;
import org.junit.jupiter.api.Test;

import static util.FlatValues.*;

public class TestFlatBuilder {
  @Test
  void testBuildAll() {
    final Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    final House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);

    Object[] allParams = {ID, NAME, COORD_X, COORD_Y, DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    Flat correctFlat = Flat.builder()
        .id(ID)
        .name(NAME)
        .coordinates(coordinates)
        .creationDate(DATE)
        .area(AREA)
        .numberOfRooms(ROOMS)
        .furniture(HAS_FURNITURE)
        .newness(IS_NEW)
        .view(VIEW)
        .house(house)
        .build();
  }
}
