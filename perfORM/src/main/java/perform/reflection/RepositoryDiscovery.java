package perform.reflection;

import perform.database.repository.CrudRepository;

import java.util.HashSet;
import java.util.Set;

public class RepositoryDiscovery {
  private final ClassPathScanner classPathScanner;
  @SuppressWarnings("rawtypes")
  private Set<Class<? extends CrudRepository>> repositories;

  public RepositoryDiscovery(ClassPathScanner classPathScanner) {
    this.classPathScanner = classPathScanner;
  }

  public Set<RepositoryMetaData<?>> discover() {
    Set<RepositoryMetaData<?>> metaData = new HashSet<>();
    repositories = classPathScanner.getSubTypesOf(CrudRepository.class);
    for (Class<? extends CrudRepository<?>> repo : repositories) {
      metaData.add(new RepositoryMetaData<>(repo));
    }
    return metaData;
  }
}
