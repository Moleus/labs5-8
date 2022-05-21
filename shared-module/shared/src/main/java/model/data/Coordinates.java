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
public class Coordinates implements Serializable {
  @GreaterThan(num = -734)
  @NotNull
  @UserAccess(description = "x coordinate")
  private double x; //Значение поля должно быть больше -734
  @NotNull
  @UserAccess(description = "y coordinate")
  private Integer y; //Поле не может быть null
}