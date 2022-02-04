package app.collection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.collection.data.Coordinates;
import app.collection.data.Flat;
import app.collection.data.House;
import app.collection.data.View;
import app.exceptions.InvalidDataValues;

public class FlatBuilder {
  private Integer nextId;
  private static FlatBuilder instance;
  private FlatBuilder() {
    nextId = 0;
  }

  public static FlatBuilder createInstance() {
    instance = new FlatBuilder();
    return instance;
  }

  public static FlatBuilder getInstance() {
    return instance;
  }

  public Flat build(Integer id,
                    String name,
                    Coordinates coordinates,
                    LocalDate creationDate,
                    Integer area,
                    Long numberOfRooms,
                    Boolean furniture,
                    Boolean newness,
                    View view,
                    House house
                    ) {
    nextId = id + 1;
    return new Flat(id, name, coordinates, creationDate, area, numberOfRooms, furniture, newness, view, house);
  }

  public Flat buildAccessible(Object[] params) throws InvalidDataValues {
    if (params.length != 12) {
      throw new InvalidDataValues("Accessible data values count should be 12, but " + params.length + " were provided.");
    }
    return buildWithCustomIdAndDate(this.nextId, LocalDate.now(), params);
  }

  public Flat buildAll(Object[] params) throws InvalidDataValues {
    if (params.length != 14 ) {
      throw new InvalidDataValues("All data values count should be 14, but " + params.length + " were provided.");
    }
    Integer id = (Integer) params[0];
    String name = (String) params[1];
    Coordinates coordinates = new Coordinates((double) params[2],(Integer)params[3]);
    LocalDate creationDate = (LocalDate) params[4];
    Integer area = (Integer) params[5];
    Long numberOfRooms = (Long) params[6];
    Boolean furniture = (Boolean) params[7];
    Boolean newness = (Boolean) params[8];
    View view = (View) params[9];
    House house = new House((String) params[10], (int) params[11], (int) params[12], (Integer) params[13]);
    return build(id, name, coordinates, creationDate, area, numberOfRooms, furniture, newness, view, house);
  }

  public Flat buildWithCustomIdAndDate(Integer id, LocalDate creationDate, Object[] params) throws InvalidDataValues {
    final int ID_INDEX = 0;
    final int DATE_INDEX = 4;

    List<Object> allParams = new ArrayList<>(Arrays.asList(params));
    allParams.add(ID_INDEX, id);
    allParams.add(DATE_INDEX, creationDate);
    return buildAll(allParams.toArray());
  }
}
