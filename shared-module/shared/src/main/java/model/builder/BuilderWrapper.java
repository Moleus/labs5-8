package model.builder;

import exceptions.ValueConstraintsException;
import exceptions.ValueFormatException;
import model.GenericModel;

public interface BuilderWrapper<T extends GenericModel> {
  int getFieldsCount();

  String getDescription();

  void setValue(String value) throws ValueFormatException, ValueConstraintsException;

  void step();

  void setPosition(int index);

  int getPosition();

  T build();
}
