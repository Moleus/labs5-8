package collection;

import exceptions.ValueConstraintsException;
import exceptions.ValueFormatException;
import model.Model;
import model.ModelDto;
import model.builder.ModelBuilderWrapper;
import model.builder.ModelDtoBuilderWrapper;
import model.data.Coordinates;
import model.data.Flat;
import model.data.House;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.FlatValues.*;

public class TestFlatBuilder {
  @Test
  @Order(1)
  void testBuildAccessible() throws ValueConstraintsException {
    final Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    final House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);

    Object[] accessibleParams = {NAME, COORD_X, COORD_Y, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    Flat correctFlat = new Flat(ID, NAME, coordinates, DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);

    FlatDto testFlatDto = (FlatDto) buildFlatDto(accessibleParams);
    Flat testFlat = (Flat) ModelBuilderWrapper.fromDto(testFlatDto);
    assertEquals(correctFlat, testFlat);
  }

  @Test
  @Order(2)
  void testBuildAll() {
    final Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    final House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);

    Object[] allParams = {ID, NAME, COORD_X, COORD_Y, DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS};
    Flat correctFlat = new Flat(ID, NAME, coordinates, DATE, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);

    Flat testFlat = (Flat) buildFlat(allParams);
    assertEquals(correctFlat, testFlat);
  }

  private ModelDto buildFlatDto(Object[] params) {
    int counter = 0;
    ModelDtoBuilderWrapper builder = new ModelDtoBuilderWrapper(new ModelDtoBuilder());
    int fieldsCount = builder.getFieldsCount();

    while (counter < fieldsCount) {
      System.out.printf("Enter %s: ", builder.getDescription());
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

  private Model buildFlat(Object[] params) {
    int counter = 0;
    ModelBuilderWrapper builder = ModelBuilderWrapper.getInstance();
    int fieldsCount = builder.getFieldsCount();

    while (counter < fieldsCount) {
      System.out.printf("Enter %s: ", builder.getDescription());
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
