package processor.poet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import utils.PoetUtils;

import javax.lang.model.element.TypeElement;

public class ModelDtoBuilderPoet extends AbstractBuilderPoet {
  public ModelDtoBuilderPoet(TypeElement targetClass, ClassName builderClassName) {
    super(targetClass, builderClassName);
  }

  @Override
  protected void implementBuildMethod() {
    TypeName targetTypeName = PoetUtils.getTypeName(targetClass);
    currentBuildMethodBuilder
        .returns(targetTypeName)
        .addStatement("return new $T($L)", targetTypeName, poetUtils.constructorInitArgs(fields));

    poetBuilderClass.addMethod(currentBuildMethodBuilder.build());
  }

  @Override
  protected void builderImplementationSpecifics() {
  }
}
