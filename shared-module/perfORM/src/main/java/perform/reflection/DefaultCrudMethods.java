package perform.reflection;

public enum DefaultCrudMethods {
  FIND_ALL("findAll"),
  SAVE("save"),
  UPDATE("update"),
  DELETE("delete"),
  DELETE_ALL("deleteAll");

  private final String methodName;

  DefaultCrudMethods(String methodName) {
    this.methodName = methodName;
  }

  public static DefaultCrudMethods fromString(String methodName) {
    for (DefaultCrudMethods b : DefaultCrudMethods.values()) {
      if (b.methodName.equalsIgnoreCase(methodName)) {
        return b;
      }
    }
    throw new IllegalArgumentException("No constant with text " + methodName + " found");
  }

  @Override
  public String toString() {
    return methodName;
  }
}
