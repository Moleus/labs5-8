package perform.reflection;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import perform.database.repository.CrudRepository;
import perform.database.repository.GenericRepositoryOperations;

import java.lang.reflect.Method;
import java.util.Set;

public class RepositoryImplementor<T, R extends CrudRepository<?>> {
  private final RepositoryMetaData<R> metaData;
  private final Class<R> repoType;
  private final GenericRepositoryOperations<T> genericRepositoryOperations;
  private Set<Method> methods;
  private DynamicType.Builder<R> builder;


  public RepositoryImplementor(RepositoryMetaData<R> metaData, GenericRepositoryOperations<T> genericRepositoryOperations) {
    this.metaData = metaData;
    this.repoType = metaData.getRepoType();
    this.genericRepositoryOperations = genericRepositoryOperations;
  }

  public Class<? extends R> implement() {
    this.builder = new ByteBuddy().subclass(repoType).name(repoType.getSimpleName() + "Repository");
    implementDefaultMethods(metaData.getDefaultMethods());
    implementFindByMethods(metaData.getFindByMethods());
    return builder.make().load(repoType.getClassLoader()).getLoaded();
  }

  private void implementDefaultMethods(Set<Method> defaultMethods) {
    for (Method method : defaultMethods) {
      processDefaultMethod(DefaultCrudMethods.valueOf(method.getName()));
    }
  }

  private void processDefaultMethod(DefaultCrudMethods method) {
    switch (method) {
      case FIND_ALL -> implementFindAll();
      case SAVE -> implementSave();
      case UPDATE -> implementUpdate();
      case DELETE -> implementDelete();
    }
  }

  private void implementFindAll() {
    String name = DefaultCrudMethods.FIND_ALL.toString();
    try {
      Method genericFindAllMethod = genericRepositoryOperations.getClass().getDeclaredMethod("findAll");
      builder = builder.method(ElementMatchers.named(name))
          .intercept(MethodCall.invoke(genericFindAllMethod));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private void implementSave() {
  }

  private void implementUpdate() {
  }

  private void implementDelete() {
  }

  private void implementFindByMethods(Set<Method> findByMethods) {
    for (Method method : findByMethods) {
    }
  }
}
