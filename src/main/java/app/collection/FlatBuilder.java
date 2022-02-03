package app.collection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.collection.data.Coordinates;
import app.collection.data.Flat;
import app.collection.data.House;
import app.collection.data.View;

public class FlatBuilder {
  private final static FlatBuilder instance = new FlatBuilder();
  public static FlatBuilder getInstance() {
    return instance;
  }

  private Integer nextId;
  private FlatBuilder() {
    nextId = 0;
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

  public Flat buildAccessible(Object[] params) {
    if (params.length != 12) {
      System.err.println("Invalid arguments number");
      // TODO: throw exception
    }
    final int ID_INDEX = 0;
    final int DATE_INDEX = 4;

    Integer id = this.nextId;
    LocalDate creationDate = LocalDate.now();
    List<Object> allParams = new ArrayList<>(Arrays.asList(params));
    allParams.add(ID_INDEX, id);
    allParams.add(DATE_INDEX, creationDate);
    return buildAll(allParams.toArray());
  }

  public Flat buildAll(Object[] params) {
    if (params.length != 14 ) {
      System.err.println("Invalid arguments number");
      // TODO: throw exception
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
}
