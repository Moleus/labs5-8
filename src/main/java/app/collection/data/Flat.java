package app.collection.data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import app.annotations.GreaterThan;
import app.annotations.NotNull;
import app.annotations.UserAccess;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
  TODO:
  1. Нужен ли здесь factory method вместо конструктора.
  3.
 */
@RequiredArgsConstructor
public final class Flat implements Comparable<Flat> {
  @NotNull
  // @Id
  // AutomaticGen
  // TODO: Проверка, что это число
  @Getter
  private final Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
  @NotNull
  @Getter
  @UserAccess(description = "flat name")
  private final String name; // Поле не может быть null, Строка не может быть пустой

  @NotNull
  @Getter
  @UserAccess(description = "flat location")
  private final Coordinates coordinates; // Поле не может быть null

  // AutomaticGen
  @NotNull
  @Getter
  private final LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                                        // автоматически
  @NotNull
  @GreaterThan
  @Getter
  @UserAccess(description = "flat area")
  private final Integer area; // Поле не может быть null, Значение поля должно быть больше 0

  @GreaterThan
  @Getter
  @UserAccess(description = "number of rooms")
  private final Long numberOfRooms; // Значение поля должно быть больше 0

  @NotNull
  @Getter
  @UserAccess(description = "is there furniture")
  private final Boolean furniture; // Поле не может быть null

  @NotNull
  @Getter
  @UserAccess(description = "is it new")
  private final Boolean newness; // Поле не может быть null

  @Getter
  @UserAccess(description = "view type")
  private final View view;

  @NotNull
  @Getter
  @UserAccess(description = "information about house")
  private final House house; // Поле не может быть null

  /**
   * Sort collection of Flats by name.
   */
  @Override
  public int compareTo(Flat o) {
    return this.getName().compareTo(o.getName());
  }

  /**
   * Two elements are equal if they have the same id because it should always be unique.
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Flat)) return false;
    Flat of = (Flat) o;
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

  @Override
  public String toString() {
    return String.format("Flat{id=%d, name=%s, coordinates=%s, creationDate=%s, area=%d, numberOfRooms=%d, furniture=%b, newness=%b, view=%s, house=%s}",
      id, name, coordinates, creationDate, area, numberOfRooms, furniture, newness, view, house);
  }

  public List<Object> getValuesRecursive() {
    return Arrays.asList(name, coordinates.getX(), coordinates.getY(), creationDate, area, numberOfRooms, furniture, newness, view, house.getName(), house.getName(), house.getNumberOfFloors(), house.getNumberOfLifts());
  }
}