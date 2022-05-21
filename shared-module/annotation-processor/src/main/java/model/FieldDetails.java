package model;

import java.util.Objects;

public final class FieldDetails<T> {
  private final Class<T> type;
  private final String description;
  private final ThrowableSetter<T> setterMethod;

  public FieldDetails(Class<T> type, String description,
                      ThrowableSetter<T> setterMethod) {
    this.type = type;
    this.description = description;
    this.setterMethod = setterMethod;
  }

  public Class<T> type() {
    return type;
  }

  public String description() {
    return description;
  }

  public ThrowableSetter<T> setterMethod() {
    return setterMethod;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (FieldDetails) obj;
    return Objects.equals(this.type, that.type) &&
        Objects.equals(this.description, that.description) &&
        Objects.equals(this.setterMethod, that.setterMethod);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, setterMethod);
  }

  @Override
  public String toString() {
    return "FieldDetails[" +
        "type=" + type + ", " +
        "description=" + description + ", " +
        "setterMethod=" + setterMethod + ']';
  }

}
