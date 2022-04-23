package perform.reflection;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassPathScanner {
  private final Reflections reflections;

  public ClassPathScanner(String... rootPackages) {
    ConfigurationBuilder configuration = ConfigurationBuilder.build(ClassPathScanner.class.getPackageName());

    configuration = configuration.forPackages(rootPackages);
    configuration = configuration.setUrls(Stream.of(rootPackages).map(ClasspathHelper::forPackage).flatMap(Collection::stream).collect(Collectors.toList()));
    FilterBuilder fb = new FilterBuilder();
    for (String rootPackage : rootPackages) {
      fb.includePackage(rootPackage);
    }
    configuration = configuration.filterInputsBy(fb);

    reflections = new Reflections(configuration);
  }

  public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
    return reflections.getTypesAnnotatedWith(annotation);
  }

  public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> superType) {
    return reflections.getSubTypesOf(superType);
  }
}
