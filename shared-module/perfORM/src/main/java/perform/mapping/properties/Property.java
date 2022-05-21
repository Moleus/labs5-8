package perform.mapping.properties;

public interface Property<T> {
  String getName();

  Class<T> getType();
}
