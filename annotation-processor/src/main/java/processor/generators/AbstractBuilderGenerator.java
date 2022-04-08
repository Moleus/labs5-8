package processor.generators;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import utils.CompileUtils;

import javax.lang.model.element.TypeElement;
import java.io.IOException;

public abstract class AbstractBuilderGenerator implements BuilderGenerator {
  protected final TypeElement targetClass;
  protected final CompileUtils compileUtils;

  AbstractBuilderGenerator(TypeElement targetClass, CompileUtils compileUtils) {
    this.targetClass = targetClass;
    this.compileUtils = compileUtils;
  }

  @Override
  abstract public void generateBuilderClass() throws IOException;

  protected abstract ClassName builderClassName();

  protected void writeJavaFile(String targetPackage, TypeSpec typeSpec) throws IOException {
    JavaFile.Builder builder = JavaFile.builder(targetPackage, typeSpec);
    JavaFile javaFile = builder.build();
    compileUtils.writeFile(javaFile);
  }
}