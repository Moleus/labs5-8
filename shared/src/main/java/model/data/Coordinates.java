package model.data;

import annotations.UserAccess;
import lombok.Data;
import perform.annotations.Collectible;
import perform.annotations.GreaterThan;
import perform.annotations.NotNull;

import java.io.Serializable;

@Data
@Collectible
public class Coordinates implements Serializable {
  @GreaterThan(num = -734)
  @NotNull
  @UserAccess(description = "x coordinate")
  private final double x; //Значение поля должно быть больше -734
  @NotNull
  @UserAccess(description = "y coordinate")
  private final Integer y; //Поле не может быть null
}