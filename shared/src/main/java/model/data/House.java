package model.data;

import annotations.Collectible;
import annotations.GreaterThan;
import annotations.NotNull;
import annotations.UserAccess;
import lombok.Data;

import java.io.Serializable;

@Data
@Collectible
public class House implements Serializable {
  @NotNull
  @UserAccess(description = "house name")
  private final String name; //Поле не может быть null
  @GreaterThan()
  @UserAccess(description = "house year")
  private final int year; //Значение поля должно быть больше 0
  @GreaterThan()
  @UserAccess(description = "number of floors in house")
  private final int numberOfFloors; //Значение поля должно быть больше 0
  @GreaterThan()
  @UserAccess(description="number of lifts in house")
  private final Integer numberOfLifts; //Значение поля должно быть больше 0
}
