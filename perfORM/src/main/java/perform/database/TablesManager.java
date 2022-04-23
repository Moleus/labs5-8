package perform.database;

import lombok.extern.log4j.Log4j2;
import perform.database.table.TableProvider;
import perform.exception.PerformException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Log4j2
public class TablesManager {
  public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
  private final List<Class<?>> entites;
  private final PreparedStatementProvider provider;

  public TablesManager(List<Class<?>> entites, PreparedStatementProvider provider) {
    this.entites = entites;
    this.provider = provider;
  }

  public void createTables() {
    for (Class<?> entity : entites) {
      TableProvider tableProvider = new TableProvider(entity);
      createTable(tableProvider);
    }
  }

  private void createTable(TableProvider tableProvider) {
    String query = CREATE_TABLE + tableProvider.getTable().getSqlRepresentation();
    PreparedStatement ps = provider.prepareStatement(query);
    try {
      ps.execute();
    } catch (SQLException e) {
      String message = "Failed to create table " + tableProvider.getTable().getTableName();
      log.error(message);
      throw new PerformException(message + ": " + e.getMessage());
    }
  }
}