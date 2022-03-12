package model.builder;

import exceptions.ValueConstraintsException;
import exceptions.ValueFormatException;
import model.FieldDetails;
import model.GenericModel;
import model.InputValueParser;

import java.util.List;

public abstract class AbstractBuilderWrapper<T extends GenericModel> implements BuilderWrapper<T> {
  private final List<FieldDetails<?>> modelFieldsInfo;
  private FieldDetails currentFieldInfo;
  protected int currentPosition = 0;

  protected AbstractBuilderWrapper(List<FieldDetails<?>> modelFieldsInfo) {
    this.modelFieldsInfo = modelFieldsInfo;
    this.currentFieldInfo = modelFieldsInfo.get(0);
  }

  @Override
  public int getFieldsCount() {
    return modelFieldsInfo.size();
  }

  @Override
  public String getDescription() {
    currentFieldInfo = modelFieldsInfo.get(currentPosition);
    return currentFieldInfo.description();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setValue(String input) throws ValueFormatException, ValueConstraintsException {
    currentFieldInfo = modelFieldsInfo.get(currentPosition);
    Class<?> fieldType = currentFieldInfo.type();
    InputValueParser fieldsValidator = new InputValueParser(input, fieldType);
    Object value = fieldsValidator.parse();
    currentFieldInfo.setterMethod().accept(fieldType.isPrimitive() ? value : fieldType.cast(value));
  }

  @Override
  public void setPosition(int index) {
    currentPosition = index;
  }

  @Override
  public int getPosition() {
    return currentPosition;
  }

  @Override
  public void step() {
    currentPosition++;
  }

  public abstract T build();
}
