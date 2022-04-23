package perform.exception;

import java.sql.SQLException;

public class JDBCException extends PerformException {
  public JDBCException(String message, SQLException cause) {
    super(message, cause);
  }
}
