package perform.mapping;

import perform.mapping.properties.EntityPersistentProperty;

import java.util.Map;

public class EntityClassesRegistry {
  public static final EntityClassesRegistry INSTANCE = new EntityClassesRegistry();

  private Map<Class<?>, EntityPersistentProperty<?>> entityToProperty;

  public void register(Class<?> entity) {
    entityToProperty.put(entity, new EntityPersistentProperty<>(entity));
  }

  @SuppressWarnings("unchecked")
  public <T> EntityPersistentProperty<T> getEntityProperty(Class<T> entity) {
    return (EntityPersistentProperty<T>) entityToProperty.get(entity);
  }
}