package model;

import java.util.List;

public interface GenericModelBuilder<T extends GenericModel> {
  List<FieldDetails<?>> getModelFieldsInfo();

  T build();
}
