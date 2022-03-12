package processor.poet;

import annotations.UserAccess;
import com.squareup.javapoet.*;
import exceptions.ValueConstraintsException;
import model.ModelDto;
import utils.PoetUtils;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

public class ModelBuilderPoet extends AbstractBuilderPoet {
  private MethodSpec.Builder modelFromDtoBuilder;
  private final TypeName targetTypeName;
  private final ClassName generatedDtoClass;
  final String MODEL_DTO_NAME = "modelDto";
  final String CASTED_DTO_NAME = "modelDtoObj";

  public ModelBuilderPoet(TypeElement targetClass, ClassName builderClassName, ClassName generatedDtoClass) {
    super(targetClass, builderClassName);
    targetTypeName = PoetUtils.getTypeName(targetClass);
    this.generatedDtoClass = generatedDtoClass;
  }

  @Override
  protected void implementBuildMethod() {
    TypeName listOfInteger = ParameterizedTypeName.get(List.class, Integer.class);
    FieldSpec nextId = FieldSpec.builder(int.class, "nextId", Modifier.PRIVATE).initializer("1").build();
    FieldSpec usedIds = FieldSpec.builder(listOfInteger, "usedIds", Modifier.PRIVATE, Modifier.FINAL)
        .initializer("new $T<>()", ArrayList.class)
        .build();

    addIdIncrementerBlock(nextId, usedIds);
    addReturnStatement();
    poetBuilderClass.addField(nextId);
    poetBuilderClass.addField(usedIds);
    poetBuilderClass.addMethod(currentBuildMethodBuilder.build());
  }

  private void addIdIncrementerBlock(FieldSpec nextId, FieldSpec usedIds) {
    Name idFieldName = fields.get(0).getSimpleName();
    currentBuildMethodBuilder
        .beginControlFlow("do")
        .addStatement("$N++", nextId)
        .endControlFlow("while ($N.contains($N))", usedIds, nextId)
        .addStatement("$N.add($N)", usedIds, idFieldName);
  }

  private void addReturnStatement() {
    currentBuildMethodBuilder
        .returns(targetTypeName)
        .addStatement("return new $T($L)", targetTypeName, poetUtils.constructorInitArgs(fields));
  }

  @Override
  protected void builderImplementationSpecifics() {
    if (generatedDtoClass != null) {
      implementModelFromDtoMethod();
      implementConstructor();
    }
  }

  private void implementModelFromDtoMethod() {
    initModelFromDtoBuilder();
    checkInstanceOf();
    setIdAndCreationDate();
    for (VariableElement field : poetUtils.getFieldToRecursivePath().keySet()) {
      if (field.getAnnotation(UserAccess.class) == null) continue;
      String dtoGetter = poetUtils.getNestedGetters().get(field);
      String builderSetter = PoetUtils.setterNameOf(poetUtils.getFieldToFlattenName().get(field));
      modelFromDtoBuilder.addStatement("$L($L.$L)", builderSetter, CASTED_DTO_NAME, dtoGetter);
    }
    modelFromDtoBuilder.addStatement("return build()");
    poetBuilderClass.addMethod(modelFromDtoBuilder.build());
  }

  private void setIdAndCreationDate() {
    modelFromDtoBuilder.addStatement("setId(this.nextId)")
        .addStatement("setCreationDate(LocalDate.now())");
  }

  private void checkInstanceOf() {
    modelFromDtoBuilder.beginControlFlow("if (!($L instanceof $T $L))", MODEL_DTO_NAME, generatedDtoClass, CASTED_DTO_NAME)
        .addStatement("throw new $T()", IllegalArgumentException.class)
        .endControlFlow();
  }

  private void initModelFromDtoBuilder() {
    final String FROM_DTO_METHOD_NAME = "fromDto";
    modelFromDtoBuilder = MethodSpec.methodBuilder(FROM_DTO_METHOD_NAME)
        .addModifiers(Modifier.PUBLIC)
        .addException(ValueConstraintsException.class)
        .returns(targetTypeName)
        .addParameter(ModelDto.class, MODEL_DTO_NAME);
  }

  private void implementConstructor() {
    poetBuilderClass.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addStatement("this.id = 0").build());
  }
}
