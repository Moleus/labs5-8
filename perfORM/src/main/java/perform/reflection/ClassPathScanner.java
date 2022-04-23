package perform.reflection;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassPathScanner {
  private final Reflections reflections;

  public ClassPathScanner(String rootPackage) {
    ConfigurationBuilder configuration = ConfigurationBuilder.build(ClassPathScanner.class.getClassLoader());

    configuration = configuration.forPackages(rootPackage);
    configuration = configuration.setUrls(ClasspathHelper.forPackage(rootPackage));
    configuration = configuration.filterInputsBy(new FilterBuilder().includePackage(rootPackage));

    reflections = new Reflections(configuration);
  }

  public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
    return reflections.getTypesAnnotatedWith(annotation);
  }

  public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> superType) {
    return reflections.getSubTypesOf(superType);
  }
}
