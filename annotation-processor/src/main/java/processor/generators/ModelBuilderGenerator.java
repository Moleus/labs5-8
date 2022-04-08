package processor.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import processor.poet.ModelBuilderPoet;
import utils.CompileUtils;

import javax.lang.model.element.TypeElement;
import java.io.IOException;

public class ModelBuilderGenerator extends AbstractBuilderGenerator {

  private final ClassName generatedDtoClass;

  public ModelBuilderGenerator(TypeElement targetClass, CompileUtils compileUtils, ClassName generatedDtoClass) {
    super(targetClass, compileUtils);
    this.generatedDtoClass = generatedDtoClass;
  }

  @Override
  public void generateBuilderClass() throws IOException {
    ModelBuilderPoet modelBuilderPoet = new ModelBuilderPoet(targetClass, builderClassName(), generatedDtoClass);
    TypeSpec generatedBuilderClass = modelBuilderPoet.getGeneratedBuilderClass();
    String targetPackage = compileUtils.getPackageName(targetClass);
    writeJavaFile(targetPackage, generatedBuilderClass);
  }

  @Override
  protected ClassName builderClassName() {
    return ClassName.get(compileUtils.getPackageName(targetClass), "Model" + "Builder");
  }
}
