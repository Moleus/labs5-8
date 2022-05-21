package perform.reflection;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import perform.database.repository.CrudRepository;
import perform.mapping.EntitiesPropertyRegistry;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositoryMetaDataTest {
  @Getter
  @Setter
  private static class TestingEntity {
    long id;
  }

  private interface TestingEntityRepository extends CrudRepository<TestingEntity> {

  }

  @Test
  public void getEntityType_ShouldBeInt_WhenExtendingRepo() {
    EntitiesPropertyRegistry.INSTANCE.register(TestingEntity.class);
    RepositoryMetaData<TestingEntity, TestingEntityRepository> repositoryMetaData = new RepositoryMetaData<>(TestingEntityRepository.class);
    assertEquals(TestingEntity.class, repositoryMetaData.getEntityType());
  }
}