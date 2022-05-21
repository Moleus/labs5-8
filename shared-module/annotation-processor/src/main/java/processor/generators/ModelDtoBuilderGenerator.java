package processor.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import processor.poet.BuilderPoet;
import processor.poet.ModelDtoBuilderPoet;
import utils.CompileUtils;

import javax.lang.model.element.TypeElement;
import java.io.IOException;

public class ModelDtoBuilderGenerator extends AbstractBuilderGenerator {
  public ModelDtoBuilderGenerator(TypeElement targetClass, CompileUtils compileUtils) {
    super(targetClass, compileUtils);
  }

  @Override
  public void generateBuilderClass() throws IOException {
    BuilderPoet modelBuilderPoet = new ModelDtoBuilderPoet(targetClass, builderClassName());
    TypeSpec generatedBuilderClass = modelBuilderPoet.getGeneratedBuilderClass();
    String targetPackage = compileUtils.getPackageName(targetClass);
    writeJavaFile(targetPackage, generatedBuilderClass);
  }

  @Override
  protected ClassName builderClassName() {
    return ClassName.get(compileUtils.getPackageName(targetClass), "ModelDto" + "Builder");
  }

}
