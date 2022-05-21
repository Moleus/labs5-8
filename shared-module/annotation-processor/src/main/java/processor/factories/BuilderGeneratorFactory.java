package processor.factories;

import annotations.Collectible;
import annotations.GenModelBuilder;
import com.squareup.javapoet.ClassName;
import model.GenericModel;
import processor.generators.BuilderGenerator;
import processor.generators.ModelBuilderGenerator;
import processor.generators.ModelDtoBuilderGenerator;
import utils.CompileUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class BuilderGeneratorFactory {
  private final CompileUtils compileUtils;

  public BuilderGeneratorFactory(CompileUtils compileUtils) {
    this.compileUtils = compileUtils;
  }

  public BuilderGenerator forElement(Element annotatedElement, ClassName generatedDtoClass) {
    if (!CompileUtils.isClass(annotatedElement)) {
      throw new IllegalArgumentException("@Model can only be placed on classes");
    }

    TypeElement targetClass = (TypeElement) annotatedElement;

    if (!compileUtils.isAssignableFrom(targetClass, GenericModel.class)) {
      throw new IllegalArgumentException("Class should implement GenericModel interface");
    }

    if (!compileUtils.isAnnotatedWith(targetClass, Collectible.class)) {
      throw new IllegalArgumentException("Class should be annotated Collectible annotation");
    }

    if (!compileUtils.isAnnotatedWith(annotatedElement, GenModelBuilder.class)) {
      throw new IllegalArgumentException("Class should be annotated with ModelBuilder annotation");
    }

    GenModelBuilder modelAnnotation = annotatedElement.getAnnotation(GenModelBuilder.class);
    switch (modelAnnotation.type()) {
      case FULL_MODEL:
        return new ModelBuilderGenerator(targetClass, compileUtils, generatedDtoClass);
      case DTO_MODEL:
        return new ModelDtoBuilderGenerator(targetClass, compileUtils);
      default:
        throw new IllegalArgumentException();
    }
  }
}
