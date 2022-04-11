package model.data;

import annotations.Collectible;
import annotations.GenDto;
import annotations.UserAccess;
import lombok.Data;
import perform.annotations.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable object class used as an entry in collection.
 * Compared with others for sorting by a name field.
 * Equivalance is checked by an id field.
 */
@GenDto(annotatedWith = UserAccess.class)
@Collectible
@Table(name = "flats")
@Entity
@Data
public final class Flat implements Serializable, Model {

  @ForeignKey(tableName = "users")
  private long userId;

  @NotNull
  @Id
//  @GeneratedValue(strategy=GenerationType.SEQUENCE)
  @GreaterThan
  private long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

  @NotNull
//  @Column(name="FLAT_NAME", length=, nullable=, unique)
  @UserAccess(description = "flat name")
  private String name; // Поле не может быть null, Строка не может быть пустой

  @NotNull
  @UserAccess(description = "flat location")
  @Embedded
  private Coordinates coordinates; // Поле не может быть null

  // AutomaticGen
  @NotNull
  private LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
  // автоматически
  @NotNull
  @GreaterThan
  @UserAccess(description = "flat area")
  private Integer area; // Поле не может быть null, Значение поля должно быть больше 0

  @GreaterThan
  @UserAccess(description = "number of rooms")
  private Long numberOfRooms; // Значение поля должно быть больше 0

  @NotNull
  @UserAccess(description = "is there furniture")
  private Boolean furniture; // Поле не может быть null

  @NotNull
  @UserAccess(description = "is it new")
  private Boolean newness; // Поле не может быть null

  //  @Enumerated(EnumType.STRING)
  @UserAccess(description = "view type")
  private View view;

  @NotNull
  @UserAccess(description = "information about house")
  @Embedded
  private House house; // Поле не может быть null

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
    return Objects.equals(this.getId(), (of.getId()));
  }

  /**
   * Used in Set/Map to check equivalence. Overridden by analogy to equals method.
   */
  @Override
  public int hashCode() {
    return (int) this.id;
  }
}