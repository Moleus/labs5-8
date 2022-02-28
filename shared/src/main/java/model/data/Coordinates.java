package model.data;

import annotations.UserAccess;
import annotations.GreaterThan;
import annotations.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Coordinates implements Collectible {
  @GreaterThan(num=-734)
  @Getter
  @NotNull
  @UserAccess(description="x coordinate")
  private final double x; //Значение поля должно быть больше -734
  @NotNull
  @Getter
  @UserAccess(description="y coordinate")
  private final Integer y; //Поле не может быть null

  @Override
  public String toString() {
    return String.format("Coordinates{(%.2f,%d)}", x, y);
  }
}