package perform.util;

import perform.exception.PerformException;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PreparedStatementUtil {
  private static final Map<Type, JDBCType> javaTypeToSql = new HashMap<>();

  static {
    javaTypeToSql.put(short.class, JDBCType.INTEGER);
    javaTypeToSql.put(int.class, JDBCType.INTEGER);
    javaTypeToSql.put(long.class, JDBCType.INTEGER);
    javaTypeToSql.put(float.class, JDBCType.FLOAT);
    javaTypeToSql.put(double.class, JDBCType.DOUBLE);
    javaTypeToSql.put(boolean.class, JDBCType.BOOLEAN);
    javaTypeToSql.put(Short.class, JDBCType.SMALLINT);
    javaTypeToSql.put(Integer.class, JDBCType.INTEGER);
    javaTypeToSql.put(Long.class, JDBCType.BIGINT);
    javaTypeToSql.put(Float.class, JDBCType.FLOAT);
    javaTypeToSql.put(Double.class, JDBCType.DOUBLE);
    javaTypeToSql.put(String.class, JDBCType.VARCHAR);
    javaTypeToSql.put(LocalDateTime.class, JDBCType.TIMESTAMP);
    javaTypeToSql.put(Boolean.class, JDBCType.BOOLEAN);
  }

  public static JDBCType getSqlType(Class<?> javaType) {
    if (javaTypeToSql.containsKey(javaType)) {
      return javaTypeToSql.get(javaType);
    }
    throw new IllegalArgumentException("Can't find JDBC equivalent for java type [" + javaType.getSimpleName() + "]");
  }

  public static void setValue(PreparedStatement ps, int index, JDBCType sqlType, Object value) throws SQLException {
    if (value instanceof Enum<?> enumValue) {
      value = enumValue.name();
    }
    ps.setObject(index, value, sqlType);
  }

  public static long getGeneratedId(PreparedStatement ps) {
    try {
      ResultSet generatedKeys = ps.getGeneratedKeys();
      if (generatedKeys.next()) {
        return generatedKeys.getLong(1);
      } else {
        throw new PerformException("Failed to retrieve generated Id");
      }
    } catch (SQLException e) {
      throw new PerformException("Failed to retrieve generated Id", e);
    }
  }
}