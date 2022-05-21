package perform.reflection;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.matcher.ElementMatchers;
import perform.database.repository.CrudRepository;
import perform.database.repository.GenericRepositoryOperations;
import perform.mapping.properties.EntityProperty;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

public class RepositoryImplementor<T, R extends CrudRepository<T>> {
  private final RepositoryMetaData<T, R> metaData;
  private final Class<R> repoType;
  private final EntityProperty<T> entityProperty;
  private final GenericRepositoryOperations<T> genericRepositoryOperations;
  private DynamicType.Builder<R> builder;


  public RepositoryImplementor(RepositoryMetaData<T, R> metaData, GenericRepositoryOperations<T> genericRepositoryOperations) {
    this.metaData = metaData;
    this.repoType = metaData.getRepoType();
    this.entityProperty = metaData.getEntityProperty();
    this.genericRepositoryOperations = genericRepositoryOperations;
  }

  public Class<? extends R> implement() {
    this.builder = new ByteBuddy().subclass(repoType).name(repoType.getSimpleName() + "Impl");
    implementDefaultMethods(metaData.getDefaultMethods());
    implementFindByMethods(metaData.getParamToFindByMethod());
    return builder.make().load(repoType.getClassLoader()).getLoaded();
  }

  private void implementDefaultMethods(Set<Method> defaultMethods) {
    for (Method method : defaultMethods) {
      processDefaultMethod(DefaultCrudMethods.fromString(method.getName()));
    }
  }

  private void processDefaultMethod(DefaultCrudMethods method) {
    switch (method) {
      case FIND_ALL: implementFindAll(); break;
      case SAVE: implementSave(); break;
      case UPDATE: implementUpdate(); break;
      case DELETE: implementDelete(); break;
    }
  }

  private void implementFindAll() {
    String name = DefaultCrudMethods.FIND_ALL.toString();
    try {
      Method genericFindAllMethod = genericRepositoryOperations.getClass().getDeclaredMethod(name);
      builder = builder.method(named(name).and(ElementMatchers.returns(List.class)).and(takesArguments(0)))
          .intercept(MethodCall.invoke(genericFindAllMethod).on(genericRepositoryOperations));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private void implementSave() {
    String name = DefaultCrudMethods.SAVE.toString();
    try {
      Method genericFindAllMethod = genericRepositoryOperations.getClass().getDeclaredMethod(name, Object.class);
      builder = builder.method(named(name))
          .intercept(MethodCall.invoke(genericFindAllMethod).on(genericRepositoryOperations).withAllArguments());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private void implementUpdate() {
    String name = DefaultCrudMethods.UPDATE.toString();
    interceptByMethodName(name);
  }

  private void implementDelete() {
    String name = DefaultCrudMethods.DELETE.toString();
    interceptByMethodName(name);
  }

  private void interceptByMethodName(String name) {
    try {
      Method genericDeleteMethod = genericRepositoryOperations.getClass().getDeclaredMethod(name, Object.class, String.class, Method.class);
      String columnName = entityProperty.getIdProperty().getColumnName();
      Method getter = entityProperty.getIdProperty().getGetter();
      builder = builder.method(named(name))
          .intercept(MethodCall.invoke(genericDeleteMethod)
              .on(genericRepositoryOperations).withAllArguments().with(columnName).with(getter));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private void implementFindByMethods(Map<String, Method> findByMethods) {
    for (Map.Entry<String, Method> colNameToMethod : findByMethods.entrySet()) {
      String columnName = colNameToMethod.getKey();
      String methodName = colNameToMethod.getValue().getName();
      builder = builder.method(named(methodName))
          .intercept(MethodCall.invoke(named("genericFindBy"))
              .on(genericRepositoryOperations)
              .withAllArguments()
              .with(columnName)
              .withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC)
          );
    }
  }
}
