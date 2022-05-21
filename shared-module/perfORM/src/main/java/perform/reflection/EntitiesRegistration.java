package perform.reflection;

import perform.annotations.Entity;
import perform.mapping.EntitiesPropertyRegistry;

import java.util.Set;

public class EntitiesRegistration {
  private final ClassPathScanner classPathScanner;
  private final EntitiesPropertyRegistry registry;

  public EntitiesRegistration(ClassPathScanner classPathScanner, EntitiesPropertyRegistry registry) {
    this.classPathScanner = classPathScanner;
    this.registry = registry;
  }

  public void registryEntities() {
    Set<Class<?>> entityTypes = classPathScanner.getTypesAnnotatedWith(Entity.class);
    for (Class<?> entityType : entityTypes) {
      registry.register(entityType);
    }
  }
}
