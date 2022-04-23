package perform.database.repository;

import java.util.List;

public interface CrudRepository<T> {
  /**
   * Returns an entity by Id.
   */
  T findById(long id);

  /**
   * Returns all entities from a table
   */
  List<T> findAll();

  /**
   * Saves an entity in a table and assignes new Id.
   */
  long save(T entity);

  /**
   * Updates columns in a table for passed entity.
   */
  boolean update(T entity);

  /**
   * Deletes an entity from a table
   */
  void delete(T entity);
}