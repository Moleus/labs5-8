package processor.generators;

import annotations.Collectible;
import annotations.GenDto;
import annotations.GenModelBuilder;
import annotations.ModelType;
import com.squareup.javapoet.*;
import lombok.Data;
import model.GenericModel;
import model.ModelDto;
import utils.CompileUtils;
import utils.PoetUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoGenerator {
  private final Class<? extends Annotation> annotatedWith;
  private final CompileUtils compileUtils;
  private TypeElement targetClass;
  private TypeSpec.Builder dtoClassBuilder;

  public DtoGenerator(Class<? extends Annotation> annotatedWith, CompileUtils compileUtils) {
    this.annotatedWith = annotatedWith;
    this.compileUtils = compileUtils;
  }

  public ClassName generateDto(Element annotatedElement) throws IOException {
    checkTargetCorrectness(annotatedElement);
    targetClass = (TypeElement) annotatedElement;
    TypeSpec dtoClass = createDtoPoet();
    String targetPackage = compileUtils.getPackageName(targetClass);
    JavaFile javaFile = JavaFile.builder(targetPackage, dtoClass).build();
    ClassName className = ClassName.get(targetPackage, dtoClass.name);
    compileUtils.writeFile(javaFile);
    return className;
  }

  private TypeSpec createDtoPoet() {
    initClassHeader();
    addFields();
    return dtoClassBuilder.build();
  }

  private void initClassHeader() {
    AnnotationSpec builderAnnotation = AnnotationSpec.builder(GenModelBuilder.class)
        .addMember("type", "$T.$L", ModelType.class, ModelType.DTO_MODEL.name())
        .build();
    dtoClassBuilder = TypeSpec.classBuilder(getDtoName())
        .addSuperinterface(ModelDto.class)
        .addSuperinterface(Serializable.class)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Data.class)
        .addAnnotation(Collectible.class)
        .addAnnotation(builderAnnotation);
  }

  private String getDtoName() {
    return getName(targetClass) + "Dto";
  }

  private void addFields() {
    for (VariableElement field : PoetUtils.getFieldsFromClass(targetClass)) {
      Set<AnnotationSpec> annotations = field.getAnnotationMirrors().stream().map(AnnotationSpec::get).collect(Collectors.toSet());
      if (compileUtils.isAnnotatedWith(field, annotatedWith)) {
        FieldSpec dtoField = FieldSpec.builder(PoetUtils.getTypeName(field), getName(field), Modifier.PRIVATE, Modifier.FINAL)
            .addAnnotations(annotations).build();
        dtoClassBuilder.addField(dtoField);
      }
    }
  }

  private String getName(Element element) {
    return element.getSimpleName().toString();
  }

  private void checkTargetCorrectness(Element annotatedElement) {
    if (!CompileUtils.isClass(annotatedElement)) {
      throw new IllegalArgumentException("@GenDto can only be placed on classes");
    }

    TypeElement targetClass = (TypeElement) annotatedElement;

    String CLASS_NAME = targetClass.getSimpleName().toString();

    if (!compileUtils.isAssignableFrom(targetClass, GenericModel.class)) {
      throw new IllegalArgumentException(CLASS_NAME + " should implement GenericModel interface");
    }

    if (!compileUtils.isAnnotatedWith(targetClass, Collectible.class)) {
      throw new IllegalArgumentException(CLASS_NAME + " should be annotated with Collectible annotation");
    }

    if (!compileUtils.isAnnotatedWith(annotatedElement, GenDto.class)) {
      throw new IllegalArgumentException(CLASS_NAME + " Class should be annotated with ModelBuilder annotation");
    }
  }
}
