package perform.reflection;

public enum DefaultCrudMethods {
  FIND_ALL("findAll"),
  SAVE("save"),
  UPDATE("update"),
  DELETE("delete");

  private final String methodName;

  DefaultCrudMethods(String methodName) {
    this.methodName = methodName;
  }

  @Override
  public String toString() {
    return methodName;
  }
}
