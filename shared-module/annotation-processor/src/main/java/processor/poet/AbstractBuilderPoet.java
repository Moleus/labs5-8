package processor.poet;

import annotations.UserAccess;
import com.squareup.javapoet.*;
import exceptions.ValueConstraintsException;
import model.FieldDetails;
import model.GenericModelBuilder;
import perform.annotations.GreaterThan;
import perform.annotations.NotNull;
import utils.PoetUtils;
import utils.Utils;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.PoetUtils.getFieldsFromClass;

public abstract class AbstractBuilderPoet implements BuilderPoet {
  private final String FIELDS_INFO_FIELD_NAME = "modelFieldsInfo";
  private final Class<?> BUILDER_SUPERINTERFACE = GenericModelBuilder.class;
  private final TypeName FIELDS_INFO_TYPE = getFieldsInfoTypeName();

  private final ClassName builderClassName;

  protected final TypeElement targetClass;
  protected final List<VariableElement> fields;
  protected final PoetUtils poetUtils;

  protected TypeSpec.Builder poetBuilderClass;
  protected MethodSpec.Builder currentSetterBuilder;
  protected VariableElement currentField;
  protected String currentFieldName;
  protected MethodSpec.Builder currentBuildMethodBuilder;

  private static TypeName getFieldsInfoTypeName() {
    TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);
    TypeName fieldDetails = ParameterizedTypeName.get(ClassName.get(FieldDetails.class), wildcard);
    return ParameterizedTypeName.get(ClassName.get(List.class), fieldDetails);
  }

  AbstractBuilderPoet(TypeElement targetClass, ClassName builderClassName) {
    this.targetClass = targetClass;
    this.builderClassName = builderClassName;
    this.fields = getFieldsFromClass(targetClass);
    poetUtils = new PoetUtils(targetClass);
  }


  @Override
  public TypeSpec getGeneratedBuilderClass() {
    this.poetBuilderClass = TypeSpec.classBuilder(builderClassName)
        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(BUILDER_SUPERINTERFACE), TypeName.get(targetClass.asType())))
        .addModifiers(Modifier.PUBLIC);
    createFieldsInfoField();
    createInfoInitBlockAndSetters();
    implementFieldsInfoMethod();
    createBuildMethodBuilder();
    implementBuildMethod();
    builderImplementationSpecifics();
    return poetBuilderClass.build();
  }

  private void createFieldsInfoField() {
    FieldSpec fieldsInfo = FieldSpec.builder(FIELDS_INFO_TYPE, FIELDS_INFO_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL)
        .initializer("new $T<>()", ArrayList.class)
        .build();
    poetBuilderClass.addField(fieldsInfo);
  }

  private void createInfoInitBlockAndSetters() {
    CodeBlock.Builder codeBuilder = CodeBlock.builder();
    for (Map.Entry<VariableElement, String> flatten : poetUtils.getFieldToFlattenName().entrySet()) {
      currentField = flatten.getKey();
      currentFieldName = Utils.decapitalize(flatten.getValue());
      String setterName = PoetUtils.setterNameOf(currentFieldName);
      createSetter(setterName);
      createValueField();
      newInitBlockEntry(codeBuilder, currentField, setterName);
    }
    poetBuilderClass.addInitializerBlock(codeBuilder.build());
  }

  private void createValueField() {
    poetBuilderClass.addField(TypeName.get(currentField.asType()), currentFieldName, Modifier.PRIVATE);
  }

  private void createSetter(String setterName) {
    createSetterBuilder(setterName);
    setterHeader();
    createConstraints();
    initializeFieldVariable();
    poetBuilderClass.addMethod(currentSetterBuilder.build());
  }

  private void createSetterBuilder(String methodName) {
    currentSetterBuilder = MethodSpec.methodBuilder(methodName);
  }

  private void setterHeader() {
    currentSetterBuilder.addModifiers(Modifier.PUBLIC)
        .returns(void.class)
        .addParameter(TypeName.get(currentField.asType()), currentFieldName)
        .addException(ValueConstraintsException.class);
  }

  private void createConstraints() {
    NotNull notNullAnnotation = currentField.getAnnotation(NotNull.class);
    if (notNullAnnotation != null && !currentField.asType().getKind().isPrimitive()) {
      notNullConditionThrow();
    }
    GreaterThan greaterThanAnnotation = currentField.getAnnotation(GreaterThan.class);
    if (greaterThanAnnotation != null) {
      lessOrEqualConditionThrow(greaterThanAnnotation.num());
    }
  }

  private void notNullConditionThrow() {
    String errorMessage = currentFieldName + " can't be null";
    currentSetterBuilder.beginControlFlow("if ($N == null)", currentFieldName)
        .addStatement("throw new $T($S)", ValueConstraintsException.class, errorMessage)
        .endControlFlow();
  }

  private void lessOrEqualConditionThrow(double higherBound) {
    String errorMessage = currentFieldName + " must be greater than " + higherBound;
    currentSetterBuilder.beginControlFlow("if ($N <= $L)", currentFieldName, higherBound)
        .addStatement("throw new $T($S)", ValueConstraintsException.class, errorMessage)
        .endControlFlow();
  }

  private void initializeFieldVariable() {
    currentSetterBuilder.addStatement("this.$N = $N", currentFieldName, currentFieldName);
  }

  private void newInitBlockEntry(CodeBlock.Builder codeBuilder, VariableElement field, String setterName) {
    String fieldType = TypeName.get(field.asType()).toString() + ".class";
    String description = getFieldDescription(field);
    String setterReference = "this::" + setterName;
    codeBuilder.add("$L.add(new $T<>($L, $S, $L));", FIELDS_INFO_FIELD_NAME, FieldDetails.class,
        fieldType, description, setterReference);
  }

  private String getFieldDescription(VariableElement field) {
    UserAccess annotation = field.getAnnotation(UserAccess.class);
    if (annotation == null) {
      return "";
    }
    String description = annotation.description();
    return description + getTypeSpecificDescription(field);
  }

  private String getTypeSpecificDescription(VariableElement field) {
    if (Boolean.class.getName().equals(field.asType().toString())) {
      return " (true/false)";
    }
    if (!field.asType().getKind().equals(TypeKind.DECLARED)) {
      return "";
    }
    Element classElement = ((DeclaredType) field.asType()).asElement();
    if (classElement.getKind().equals(ElementKind.ENUM)) {
      return " (" + String.join(", ", getEnumConstantsOf(classElement)) + ")";
    }
    return "";
  }

  private List<String> getEnumConstantsOf(Element enumElement) {
    return enumElement.getEnclosedElements().stream().filter(e -> e.getKind().equals(ElementKind.ENUM_CONSTANT)).map(PoetUtils::getName).collect(Collectors.toList());
  }

  private void implementFieldsInfoMethod() {
    String GET_FIELDS_INFO_METHOD_NAME = "getModelFieldsInfo";
    MethodSpec buildMethod = MethodSpec.methodBuilder(GET_FIELDS_INFO_METHOD_NAME)
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(FIELDS_INFO_TYPE)
        .addStatement("return $L", FIELDS_INFO_FIELD_NAME)
        .build();
    poetBuilderClass.addMethod(buildMethod);
  }

  protected void createBuildMethodBuilder() {
    String BUILD_METHOD_NAME = "build";
    currentBuildMethodBuilder = MethodSpec.methodBuilder(BUILD_METHOD_NAME)
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC);
  }

  protected abstract void implementBuildMethod();

  protected abstract void builderImplementationSpecifics();
}