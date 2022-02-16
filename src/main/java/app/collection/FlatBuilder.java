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

/**
 * This class manages creation of {@link Flat} objects.
 * It stores id of last created object and increases this value each time to avoid collisions.
 */
public class FlatBuilder {
  private int nextId;
  private final List<Integer> usedIds;
  private static FlatBuilder instance;
  private FlatBuilder() {
    nextId = 1;
    usedIds = new ArrayList<>();
  }

  /**
   * Static factory method to create new instance of this class.
   * @return new {@link Flat} instance
   */
  public static FlatBuilder createInstance() {
    instance = new FlatBuilder();
    return instance;
  }

  /**
   * Returns instance of this class.
   */
  public static FlatBuilder getInstance() {
    return instance;
  }

  /**
   * Takes all fields which are required for creating new {@link Flat} and returns new Flat instance
   */
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
    do {
      nextId++;
    } while (usedIds.contains(nextId));
    usedIds.add(id);
    return new Flat(id, name, coordinates, creationDate, area, numberOfRooms, furniture, newness, view, house);
  }

  /**
   * Builds a {@link Flat} from user passed values.
   * @param params Array of values which correspond to accessibe fields.
   * @return new {@link Flat} instance
   * @throws InvalidDataValues if params array length doesn't match the count of accessible fields.
   */
  public Flat buildAccessible(Object[] params) throws InvalidDataValues {
    if (params.length != 12) {
      throw new InvalidDataValues("Accessible data values count should be 12, but " + params.length + " were provided.");
    }
    return buildWithCustomIdAndDate(this.nextId, LocalDate.now(), params);
  }

  /**
   * Builds {@link Flat} from all params
   * @param params Array of values which correspond to all class fields.
   * @return new {@link Flat} instance
   * @throws InvalidDataValues if params array length doesn't match the count of all class fields.
   */
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

  /**
   * Builds {@link Flat} from accessible fields' values and specified id, creationDate.
   * @return new {@link Flat} instance
   * @throws InvalidDataValues if params array length doesn't match the count of all class fields.
   */
  public Flat buildWithCustomIdAndDate(Integer id, LocalDate creationDate, Object[] params) throws InvalidDataValues {
    final int ID_INDEX = 0;
    final int DATE_INDEX = 4;

    List<Object> allParams = new ArrayList<>(Arrays.asList(params));
    allParams.add(ID_INDEX, id);
    allParams.add(DATE_INDEX, creationDate);
    return buildAll(allParams.toArray());
  }
}
