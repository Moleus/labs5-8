package model.builder;

import model.ModelDto;

public class ModelDtoBuilderWrapper extends AbstractBuilderWrapper<ModelDto> {
  private final ModelDtoBuilder modelDtoBuilder;

  public ModelDtoBuilderWrapper(ModelDtoBuilder modelDtoBuilder) {
    super(modelDtoBuilder.getModelFieldsInfo());
    this.modelDtoBuilder = modelDtoBuilder;
  }

  @Override
  public ModelDto build() {
    return modelDtoBuilder.build();
  }
}