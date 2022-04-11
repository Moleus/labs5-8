package utils;

import com.squareup.javapoet.TypeName;
import lombok.Getter;
import perform.annotations.Collectible;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.*;
import java.util.stream.Collectors;

public class PoetUtils {
  @Getter
  private final LinkedHashMap<VariableElement, List<String>> fieldToRecursivePath = new LinkedHashMap<>();
  private final List<String> currentFieldRecursivePath = new ArrayList<>();
  @Getter
  private final LinkedHashMap<VariableElement, String> fieldToFlattenName;
  @Getter
  private final LinkedHashMap<VariableElement, String> nestedGetters;

  public static List<VariableElement> getFieldsFromClass(Element targetClass) {
    List<? extends Element> enclosedElements = targetClass.getEnclosedElements();
    return enclosedElements.stream().filter(e -> e.getKind().equals(ElementKind.FIELD)).map(e -> (VariableElement) e).toList();
  }

  public static TypeName getTypeName(Element element) {
    return TypeName.get(element.asType());
  }

  public static String getName(Element element) {
    return element.getSimpleName().toString();
  }

  public PoetUtils(Element targetClass) {
    getRecursiveEnclosed(targetClass);
    fieldToFlattenName = createFieldToFlattenName();
    nestedGetters = createNestedGetters();
  }

  private void getRecursiveEnclosed(Element element) {
    for (VariableElement field : getFieldsFromClass(element)) {
      currentFieldRecursivePath.add(PoetUtils.getName(field));
      if (isCollectible(field)) {
        getRecursiveEnclosed(castToDeclaredType(field));
      } else {
        fieldToRecursivePath.put(field, List.copyOf(currentFieldRecursivePath));
      }
      currentFieldRecursivePath.remove(PoetUtils.getName(field));
    }
  }

  private static Element castToDeclaredType(Element field) {
    return ((DeclaredType) field.asType()).asElement();
  }

  private static boolean isCollectible(Element field) {
    return (!field.asType().getKind().isPrimitive()) &&
        Objects.nonNull(castToDeclaredType(field).getAnnotation(Collectible.class));
  }

  /**
   * Format: {id, "getId", {x, "setCoordinatesX"}}
   */
  private LinkedHashMap<VariableElement, String> createFieldToFlattenName() {
    return fieldToRecursivePath.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> flattenOf(e.getValue()),
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
  }

  public static String setterNameOf(String fieldName) {
    return "set" + Utils.capitalize(fieldName);
  }

  private String flattenOf(List<String> fieldsPath) {
    return fieldsPath.stream().map(Utils::capitalize).collect(Collectors.joining(""));
  }

  /**
   * Format: {id, "getId"}, {x, "getCoordinates().getX()}
   */
  private LinkedHashMap<VariableElement, String> createNestedGetters() {
    return fieldToRecursivePath.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> nestedGettersOf(e.getValue()),
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
  }

  private String nestedGettersOf(List<String> fieldsPath) {
    return fieldsPath.stream().map(Utils::capitalize).collect(Collectors.joining("().get", "get", "()"));
  }

  public String constructorInitArgs(List<VariableElement> fields) {
    StringJoiner arguments = new StringJoiner(", ");
    for (VariableElement field : fields) {
      if (isCollectible(field)) {
        arguments.add(complexObjectInitialization(field));
        continue;
      }
      arguments.add(Utils.decapitalize(flattenOf(fieldToRecursivePath.get(field))));
    }
    return arguments.toString();
  }

  private String complexObjectInitialization(VariableElement field) {
    String fieldType = TypeName.get(field.asType()).toString();
    List<VariableElement> enclosedElements = getFieldsFromClass(castToDeclaredType(field));
    return String.format("new %s(%s)", fieldType, constructorInitArgs(enclosedElements));
  }
}