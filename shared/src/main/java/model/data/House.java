package model.data;

import annotations.UserAccess;
import annotations.GreaterThan;
import annotations.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class House {
  @NotNull
  @Getter
  @UserAccess(description="house name")
  private final String name; //Поле не может быть null
  @GreaterThan()
  @Getter
  @UserAccess(description="house year")
  private final int year; //Значение поля должно быть больше 0
  @GreaterThan()
  @Getter
  @UserAccess(description="number of floors in house")
  private final int numberOfFloors; //Значение поля должно быть больше 0
  @GreaterThan()
  @Getter
  @UserAccess(description="number of lifts in house")
  private final Integer numberOfLifts; //Значение поля должно быть больше 0

  @Override
  public String toString() {
    return String.format("House{name=%s, year=%d, numberOfFloors=%d, numberOfLifts=%d", name, year, numberOfFloors, numberOfLifts);
  }
}
