package server.collection;

import model.ModelDto;
import model.data.Coordinates;
import model.data.Flat;
import model.data.FlatDto;
import model.data.House;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.FlatValues.*;

class DtoToModelMapperTest {
  private static Flat correctFlat;
  private static Coordinates coordinates;
  private static House house;

  @BeforeAll
  static void prepareFlat() {
    coordinates = new Coordinates(COORD_X, COORD_Y);
    house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);
    correctFlat = new Flat();
    correctFlat.setId(ID);
    correctFlat.setName(NAME);
    correctFlat.setCoordinates(coordinates);
    correctFlat.setCreationDate(DATE);
    correctFlat.setArea(AREA);
    correctFlat.setNumberOfRooms(ROOMS);
    correctFlat.setFurniture(HAS_FURNITURE);
    correctFlat.setNewness(IS_NEW);
    correctFlat.setView(VIEW);
    correctFlat.setHouse(house);
  }

  @Test
  public void fromDto_ShouldMapDtoToModel() {
    ModelDto dto = new FlatDto(NAME, coordinates, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);
    Flat flat = DtoToModelMapper.fromDto(dto);
    flat.setId(ID);
    flat.setCreationDate(DATE);
    assertEquals(correctFlat, flat);
  }
}