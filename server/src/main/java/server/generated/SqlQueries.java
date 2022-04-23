package server.generated;

public final class SqlQueries {
  // properties
  public static final String ID_COLUMN = "id";
  public static final String TABLE_NAME = "Flats";

  // queries
  public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
  public static final String SELECT_BY_ID = SELECT_ALL + " WHERE " + ID_COLUMN + " = ?";
  public static final String SELECT_USER = SELECT_ALL + " WHERE " + "CRITERIA1" + " AND " + "CRITERIA2";
  public static final String INSERT_ENTITY = "INSERT INTO " + TABLE_NAME + " (name, cord_x, cord_y, ...) VALUES (?,?,?,...)";
  public static final String DELETE_ENTITY = "DELETE * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN + " = ?";

  public static final String USER_AND_PASSWORD_QUERY = "login = ?";
}
