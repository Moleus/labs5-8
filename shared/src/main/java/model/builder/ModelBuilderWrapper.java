package model.builder;

import exceptions.ValueConstraintsException;
import model.Model;
import model.ModelDto;
import model.data.ModelBuilder;

public class ModelBuilderWrapper extends AbstractBuilderWrapper<Model> {
  private final static ModelBuilder modelBuilder = new ModelBuilder();
  private final static ModelBuilderWrapper instance = new ModelBuilderWrapper();

  private ModelBuilderWrapper() {
    super(modelBuilder.getModelFieldsInfo());
  }

  public static ModelBuilderWrapper getInstance() {
    return instance;
  }

  public static Model fromDto(ModelDto modelDto) throws ValueConstraintsException {
    return modelBuilder.fromDto(modelDto);
  }

  @Override
  public Model build() {
    return modelBuilder.build();
  }
}
