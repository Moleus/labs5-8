package perform.util;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import perform.annotations.Entity;
import perform.exception.FieldNotFoundException;
import perform.exception.InvalidRepositoryException;
import perform.mapping.properties.EntityPersistentProperty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MethodUtilTest {
  @Entity
  @Getter
  @Setter
  private static class Car {
    long id;
    String name;
  }

  private interface UtilsRepo<Car> {
    Car findById(int notValidType);

    Car findByNonValid(long varName);

    Car findById(long validType);

    List<Car> findAll();

    long save(Car entity);
  }

  Class<?> repo = UtilsRepo.class;
  EntityPersistentProperty<Car> entity = EntityPersistentProperty.of(Car.class);

  @Test
  public void isFind_ShouldCheckMethodName() throws NoSuchMethodException {
    assertTrue(MethodUtil.isFind(repo.getMethod("findById", int.class)));
    assertFalse(MethodUtil.isFind(repo.getMethod("findAll")));
  }

  @Test
  public void isFindValid_ShouldCompareParamAndSuffix() {
    assertDoesNotThrow(
        () -> MethodUtil.checkFindMethod(entity, repo.getMethod("findById", long.class)));
    assertThrows(InvalidRepositoryException.class,
        () -> MethodUtil.checkFindMethod(entity, repo.getMethod("findById", int.class)));
    assertThrows(FieldNotFoundException.class,
        () -> MethodUtil.checkFindMethod(entity, repo.getMethod("findByNonValid", long.class)));
  }
}