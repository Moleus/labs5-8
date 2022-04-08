package model.data;

import annotations.Collectible;
import annotations.GreaterThan;
import annotations.NotNull;
import annotations.UserAccess;
import lombok.Data;

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