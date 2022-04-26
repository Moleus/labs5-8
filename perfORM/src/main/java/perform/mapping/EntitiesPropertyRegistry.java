package perform.mapping;

import perform.mapping.properties.EntityPersistentProperty;
import perform.mapping.properties.EntityProperty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Stores classes annotated with {@link perform.annotations.Entity} and {@link perform.annotations.Embeddable}
 */
public class EntitiesPropertyRegistry {
  public static final EntitiesPropertyRegistry INSTANCE = new EntitiesPropertyRegistry();

  private final Map<Class<?>, EntityPersistentProperty<?>> entityToProperty = new LinkedHashMap<>();

  public void register(Class<?> entity) {
    entityToProperty.put(entity, EntityPersistentProperty.of(entity));
  }

  @SuppressWarnings("unchecked")
  public <T> EntityProperty<T> getEntityProperty(Class<T> entity) {
    return (EntityProperty<T>) entityToProperty.get(entity);
  }

  public Set<EntityProperty<?>> getAll() {
    return Set.copyOf(entityToProperty.values());
  }
}