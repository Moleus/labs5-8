package processor;

import annotations.GenDto;
import annotations.GenModelBuilder;
import annotations.UserAccess;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import processor.factories.BuilderGeneratorFactory;
import processor.generators.DtoGenerator;
import utils.CompileUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
public class ModelProcessor extends AbstractProcessor {
  private Messager messager;
  private BuilderGeneratorFactory builderGeneratorFactory;
  private DtoGenerator dtoGenerator;
  private ClassName generatedDtoClass;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    messager = processingEnv.getMessager();
    Filer filer = processingEnv.getFiler();
    Elements elements = processingEnv.getElementUtils();
    Types types = processingEnv.getTypeUtils();
    CompileUtils compileUtils = new CompileUtils(types, elements, filer);
    builderGeneratorFactory = new BuilderGeneratorFactory(compileUtils);
    dtoGenerator = new DtoGenerator(UserAccess.class, compileUtils);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(GenDto.class)) {
      processGenDto(annotatedElement);
    }
    for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(GenModelBuilder.class)) {
      return processBuilder(annotatedElement);
    }
    return false;
  }

  private boolean processBuilder(Element targetElement) {
    try {
      builderGeneratorFactory.forElement(targetElement, generatedDtoClass).generateBuilderClass();
    } catch (IOException e) {
      error(targetElement, e.getMessage());
      return false;
    }
    return true;
  }

  private void processGenDto(Element targetElement) {
    try {
      generatedDtoClass = dtoGenerator.generateDto(targetElement);
    } catch (IOException e) {
      error(targetElement, e.getMessage());
    }

  }

  private void error(Element element, String msg) {
    messager.printMessage(Diagnostic.Kind.ERROR, msg, element);
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(GenModelBuilder.class.getCanonicalName(), GenDto.class.getCanonicalName());
  }
}
