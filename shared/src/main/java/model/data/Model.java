package model.data;

import model.GenericModel;

public interface Model extends GenericModel, Comparable<Model> {
  long getId();

  void setId(long id);
}
