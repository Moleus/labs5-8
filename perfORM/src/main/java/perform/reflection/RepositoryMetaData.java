package perform.reflection;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import perform.database.repository.CrudRepository;
import perform.database.repository.GenericRepositoryOperations;
import perform.exception.InvalidRepositoryException;
import perform.exception.PerformException;
import perform.mapping.EntitiesPropertyRegistry;
import perform.mapping.properties.EntityProperty;
import perform.util.MethodUtil;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Collect information about EntityRepository interface which implements {@link perform.database.repository.CrudRepository}.
 * Find methods which return an Entity type and take a value as parameter.
 * Usually it will be {@code T findById(long id)} or {@code T findByName(String name)}.
 * Check that parameter name and type corresponds to any column and save this pair in a map.
 * <p>
 * Note: generic implementations for default methods, such as update/delete/findAll are in
 * a separate class {@link GenericRepositoryOperations}
 */
public class RepositoryMetaData<R extends CrudRepository<?>> {
  @Getter
  private final EntityProperty<?> entityProperty;
  @Getter
  private final Class<R> repoType;
  private final Method[] declaredMethods;

  @Getter
  private final Set<Method> findByMethods = new HashSet<>();
  @Getter
  private final Set<Method> defaultMethods = new HashSet<>();

  public RepositoryMetaData(Class<R> repoType) {
    this.repoType = repoType;
    declaredMethods = repoType.getDeclaredMethods();
    Class<?> entityType = getEntityType();
    this.entityProperty = EntitiesPropertyRegistry.INSTANCE.getEntityProperty(entityType);
    groupMethods();
  }

  private void groupMethods() {
    for (Method method : declaredMethods) {
      processMethod(method);
    }
  }

  private void processMethod(Method method) {
    String methodName = method.getName();
    if (MethodUtil.isFind(method)) {
      processFindByMethod(method);
    } else if (EnumUtils.isValidEnum(DefaultCrudMethods.class, methodName)) {
      defaultMethods.add(method);
    } else {
      throw new InvalidRepositoryException("Unknown method: [" + methodName + "]", repoType);
    }
  }

  private void processFindByMethod(Method method) {
    MethodUtil.checkFindMethod(entityProperty, method);
    findByMethods.add(method);
  }

  /**
   * Returns a type which is passed as a generic parameter to CrudRepository.
   * Such as {@code MyRepo extends CrudRepository<MyType>}
   * <p>
   * note: It's package-private for tests only
   */
  Class<?> getEntityType() {
    Type[] superInterfaces = repoType.getGenericInterfaces();
    if (superInterfaces.length != 1) {
      throw new PerformException("Repository [" + repoType.getSimpleName() + "] must extend only one CrudRepository");
    }
    ParameterizedType crudRepoInterface = (ParameterizedType) superInterfaces[0];
    if (!(crudRepoInterface.getRawType().equals(CrudRepository.class))) {
      throw new PerformException("Repository [" + repoType.getSimpleName() + "] must extend CrudRepository interface");
    }
    return (Class<?>) crudRepoInterface.getActualTypeArguments()[0];
  }
}