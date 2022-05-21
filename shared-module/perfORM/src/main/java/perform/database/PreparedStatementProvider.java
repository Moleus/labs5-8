package perform.database;

import perform.exception.PerformException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class uses Sql connection to prepare statements from sql string.
 */
public class PreparedStatementProvider {
  private final Connection connection;

  public PreparedStatementProvider(Connection connection) {
    this.connection = connection;
  }

  /**
   * Prepares statement from sql which keeps generated id if the statement was an INSERT.
   */
  public PreparedStatement prepareStatement(String sql) {
    try {
      return connection.prepareStatement(sql, new String[]{"id"});
    } catch (SQLException e) {
      e.printStackTrace();
      throw new PerformException("Failed to create PS");
    }
  }

  /**
   * Use when you've executed multiple Sql queries via Prepared Statement.
   */
  public void commitChanges() {
    try {
      connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
