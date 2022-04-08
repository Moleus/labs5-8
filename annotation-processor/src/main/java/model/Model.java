package model;

import java.util.List;

public interface Model extends GenericModel, Comparable<Model> {
  List<Object> getValuesRecursive();

  Integer getId();
}
