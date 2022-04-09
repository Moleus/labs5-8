package collection;

import exceptions.ValueConstraintsException;
import exceptions.ValueFormatException;
import model.builder.ModelBuilderWrapper;
import model.data.Coordinates;
import model.data.Flat;
import model.data.House;
import model.data.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    Flat testFlat = (Flat) buildFlat(allParams);
    assertEquals(correctFlat, testFlat);
  }

  private Model buildFlat(Object[] params) {
    int counter = 0;
    ModelBuilderWrapper builder = ModelBuilderWrapper.getInstance();
    int fieldsCount = builder.getFieldsCount();

    while (counter < fieldsCount) {
      try {
        builder.setValue(String.valueOf(params[counter]));
      } catch (ValueFormatException | ValueConstraintsException e) {
        // echo that input is invalid or doesn't match constraints.
        System.out.println(e.getMessage());
        continue;
      }
      builder.step();
      counter++;
    }
    return builder.build();
  }
}
