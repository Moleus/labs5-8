package utils;

import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

public class CompileUtils {
  private final Types typesUtil;
  private final Elements elementsUtil;
  private final Filer filer;

  public CompileUtils(Types typesUtil, Elements elementsUtil, Filer filer) {
    this.typesUtil = typesUtil;
    this.elementsUtil = elementsUtil;
    this.filer = filer;
  }

  public void writeFile(JavaFile file) throws IOException {
    file.writeTo(filer);
  }

  public String getPackageName(Element targetClass) {
    PackageElement packageElement = elementsUtil.getPackageOf(targetClass);
    return packageElement.isUnnamed() ? "" : packageElement.toString();
  }

  public static boolean isClass(Element element) {
    ElementKind elementKind = element.getKind();
    return (elementKind == ElementKind.CLASS);
  }

  public boolean isAssignableFrom(TypeElement targetType, Class<?> superType) {
    TypeMirror expectedTypeMirror = elementsUtil.getTypeElement(superType.getCanonicalName()).asType();
    return typesUtil.isAssignable(targetType.asType(), expectedTypeMirror);
  }

  public boolean isAnnotatedWith(Element targetElement, Class<? extends Annotation> annotation) {
    return Objects.nonNull(targetElement.getAnnotation(annotation));
  }
}
