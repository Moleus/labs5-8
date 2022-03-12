package model.data;

import annotations.*;
import lombok.Data;
import model.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Immutable object class used as an entry in collection.
 * Compared with others for sorting by a name field.
 * Equivalance is checked by an id field.
 */
@GenModelBuilder(type = ModelType.FULL_MODEL)
@GenDto(annotatedWith = UserAccess.class)
@Collectible
@Data
public final class Flat implements Serializable, Model {
  @NotNull
  @GreaterThan
  private final Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
  @NotNull
  @UserAccess(description = "flat name")
  private final String name; // Поле не может быть null, Строка не может быть пустой

  @NotNull
  @UserAccess(description = "flat location")
  private final Coordinates coordinates; // Поле не может быть null

  // AutomaticGen
  @NotNull
  private final LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                                        // автоматически
  @NotNull
  @GreaterThan
  @UserAccess(description = "flat area")
  private final Integer area; // Поле не может быть null, Значение поля должно быть больше 0

  @GreaterThan
  @UserAccess(description = "number of rooms")
  private final Long numberOfRooms; // Значение поля должно быть больше 0

  @NotNull
  @UserAccess(description = "is there furniture")
  private final Boolean furniture; // Поле не может быть null

  @NotNull
  @UserAccess(description = "is it new")
  private final Boolean newness; // Поле не может быть null

  @UserAccess(description = "view type")
  private final View view;

  @NotNull
  @UserAccess(description = "information about house")
  private final House house; // Поле не может быть null

  /**
   * Sort collection of Flats by name.
   */
  @Override
  public int compareTo(Model o) {
    return this.getName().compareTo(((Flat) o).getName());
  }

  /**
   * Compares by id because it should always be unique.
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Flat of)) return false;
    if (this == o) return true;
    return this.getId().equals(of.getId());
  }

  /**
   * Used in Set/Map to check equivalence. Overridden by analogy to equals method.
   */
  @Override
  public int hashCode() {
    return this.id;
  }

  /**
   * Returns values extracted from all fields including objects.
   */
  @Override
  public List<Object> getValuesRecursive() {
    return Arrays.asList(id, name, coordinates.getX(), coordinates.getY(), creationDate, area, numberOfRooms, furniture, newness, view, house.getName(), house.getYear(), house.getNumberOfFloors(), house.getNumberOfLifts());
  }
}