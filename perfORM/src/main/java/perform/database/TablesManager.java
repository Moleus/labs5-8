package perform.database;

import lombok.extern.log4j.Log4j2;
import perform.database.table.RelationalTable;
import perform.database.table.TableProvider;
import perform.exception.PerformException;
import perform.mapping.properties.EntityProperty;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public class TablesManager {
  public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
  private final Set<EntityProperty<?>> entities;
  private final PreparedStatementProvider provider;

  private final Map<EntityProperty<?>, RelationalTable> entityToTable = new HashMap<>();

  public TablesManager(Set<EntityProperty<?>> entities, PreparedStatementProvider provider) {
    this.entities = entities;
    this.provider = provider;
    generateMetaTables();
  }

  /**
   * Generates table representation for each entity and stores it internally.
   */
  public void generateMetaTables() {
    for (EntityProperty<?> entity : entities) {
      TableProvider<?> tableProvider = new TableProvider<>(entity);
      RelationalTable table = tableProvider.getTable();
      entityToTable.put(entity, table);
    }
  }

  /**
   * Retrieve {@link RelationalTable} table which represents an entity.
   */
  public RelationalTable getTable(EntityProperty<?> entityProperty) {
    return entityToTable.get(entityProperty);
  }

  /**
   * Executes prepared statement queries to create tables in db.
   */
  public void createMissingTables() {
    for (RelationalTable table : entityToTable.values()) {
      createTable(table);
    }
  }

  private void createTable(RelationalTable table) {
    String query = CREATE_TABLE + table.getSqlRepresentation();
    try (PreparedStatement ps = provider.prepareStatement(query)) {
      ps.execute();
    } catch (SQLException e) {
      String message = "Failed to create table [" + table.getTableName() + "]";
      log.error(message);
      throw new PerformException(message + ": " + e.getMessage());
    }
  }
}