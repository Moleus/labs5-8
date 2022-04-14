package perform.util;

import perform.exception.PerformException;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
    javaTypeToSql.put(double.class, JDBCType.FLOAT);
    javaTypeToSql.put(boolean.class, JDBCType.BOOLEAN);
    javaTypeToSql.put(char[].class, JDBCType.VARCHAR);
    javaTypeToSql.put(Short.class, JDBCType.SMALLINT);
    javaTypeToSql.put(Integer.class, JDBCType.INTEGER);
    javaTypeToSql.put(Long.class, JDBCType.BIGINT);
    javaTypeToSql.put(Float.class, JDBCType.FLOAT);
    javaTypeToSql.put(Double.class, JDBCType.FLOAT);
    javaTypeToSql.put(String.class, JDBCType.VARCHAR);
    javaTypeToSql.put(LocalDateTime.class, JDBCType.TIMESTAMP);
    javaTypeToSql.put(LocalDate.class, JDBCType.DATE);
    javaTypeToSql.put(Boolean.class, JDBCType.BOOLEAN);
    javaTypeToSql.put(Enum.class, JDBCType.VARCHAR);
  }

  public static JDBCType getSqlType(Class<?> javaType) {
    if (Enum.class.isAssignableFrom(javaType)) {
      return javaTypeToSql.get(Enum.class);
    }
    if (javaTypeToSql.containsKey(javaType)) {
      return javaTypeToSql.get(javaType);
    }
    throw new IllegalArgumentException("Can't find JDBC equivalent for java type [" + javaType.getSimpleName() + "]");
  }

  public static void setValue(PreparedStatement ps, int index, Object value) throws SQLException {
    if (value instanceof Enum<?> enumValue) {
      value = enumValue.name();
    }
    ps.setObject(index, value);
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