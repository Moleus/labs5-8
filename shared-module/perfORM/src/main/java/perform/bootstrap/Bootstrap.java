package perform.bootstrap;

import lombok.extern.log4j.Log4j2;
import perform.cfg.Environment;
import perform.database.PreparedStatementProvider;
import perform.database.TablesManager;
import perform.database.connection.ConnectionProvider;
import perform.database.connection.ConnectionProviderInitiator;
import perform.database.query.Statements;
import perform.database.repository.CrudRepository;
import perform.database.repository.GenericRepositoryOperations;
import perform.exception.PerformException;
import perform.mapping.EntitiesPropertyRegistry;
import perform.mapping.mappers.EntityRowMapper;
import perform.mapping.mappers.EntityToRowMapper;
import perform.mapping.properties.EntityProperty;
import perform.reflection.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Log4j2
public class Bootstrap {
  public static final String[] PACKAGES_NAMES = {"model.data", "server.generated.repository", "user"};

  private final ClassPathScanner classPathScanner = new ClassPathScanner(PACKAGES_NAMES);
  private final EntitiesPropertyRegistry registry = EntitiesPropertyRegistry.INSTANCE;

  private PreparedStatementProvider psProvider;
  private final Map<Class<?>, CrudRepository<?>> entityToRepo = new HashMap<>();
  private final Map<EntityProperty<?>, GenericRepositoryOperations<?>> repoOperations = new HashMap<>();
  private TablesManager tablesManager;

  public Bootstrap() {
    initDbConnection();
    registerEntities();
    implementRepositories();
  }

  @SuppressWarnings("unchecked")
  public <T, R extends CrudRepository<T>> R getRepository(Class<T> entityType) {
    return (R) entityToRepo.get(entityType);
  }

  private void initDbConnection() {
    Properties properties = Environment.getProperties();
    ConnectionProvider connectionProvider = ConnectionProviderInitiator.INSTANCE.initiate(properties);
    Connection connection = connectionProvider.getConnection();
    psProvider = new PreparedStatementProvider(connection);
  }

  private void registerEntities() {
    EntitiesRegistration registration = new EntitiesRegistration(classPathScanner, registry);
    registration.registryEntities();
    prepareMetaEntities();
    createMissingTables();
  }

  private void createMissingTables() {
    tablesManager.createMissingTables();
  }

  private void prepareMetaEntities() {
    Set<EntityProperty<?>> allEntities = registry.getAll();
    this.tablesManager = new TablesManager(registry.getAll(), psProvider);
    for (EntityProperty<?> entityProperty : allEntities) {
      repoOperations.put(entityProperty,
          new GenericRepositoryOperations(
              psProvider,
              new Statements(tablesManager.getTable(entityProperty)),
              new EntityRowMapper<>(entityProperty),
              new EntityToRowMapper<>(entityProperty)
          )
      );
    }
  }

  private void implementRepositories() {
    RepositoryDiscovery discovery = new RepositoryDiscovery(classPathScanner);
    for (RepositoryMetaData<?, ?> metaData : discovery.discover()) {
      implementRepo(metaData);
    }
  }

  private <T, R extends CrudRepository<T>> void implementRepo(RepositoryMetaData<T, R> metaData) {
    EntityProperty<T> entityProperty = metaData.getEntityProperty();
    RepositoryImplementor<T, R> repositoryImplementor = new RepositoryImplementor<>(
        metaData,
        (GenericRepositoryOperations<T>) repoOperations.get(entityProperty));
    try {
      entityToRepo.put(entityProperty.getType(), repositoryImplementor.implement().getDeclaredConstructor().newInstance());
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new PerformException("Failed to create a new Instance of [" + entityProperty.getName() + "] Repository");
    }
  }
}
