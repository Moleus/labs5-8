package model.data;

import annotations.Collectible;
import annotations.UserAccess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import perform.annotations.GreaterThan;
import perform.annotations.NotNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Collectible
public class House implements Serializable {
  @NotNull
  @UserAccess(description = "house name")
  private String name; //Поле не может быть null
  @GreaterThan()
  @UserAccess(description = "house year")
  private int year; //Значение поля должно быть больше 0
  @GreaterThan()
  @UserAccess(description = "number of floors in house")
  private int numberOfFloors; //Значение поля должно быть больше 0
  @GreaterThan()
  @UserAccess(description = "number of lifts in house")
  private Integer numberOfLifts; //Значение поля должно быть больше 0
}
