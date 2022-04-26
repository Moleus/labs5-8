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
  private static final Map<Type, JDBCType> javaTypeToJDBC = new HashMap<>();
  private static final Map<JDBCType, String> JDBCToPgsql = new HashMap<>();

  static {
    JDBCToPgsql.put(JDBCType.BIT, "bool");
    JDBCToPgsql.put(JDBCType.BOOLEAN, "bool");
    JDBCToPgsql.put(JDBCType.BIGINT, "bigserial");
    JDBCToPgsql.put(JDBCType.BINARY, "bytea");
    JDBCToPgsql.put(JDBCType.CHAR, "char");
    JDBCToPgsql.put(JDBCType.INTEGER, "int4");
    JDBCToPgsql.put(JDBCType.SMALLINT, "int2");
    JDBCToPgsql.put(JDBCType.REAL, "float4");
    JDBCToPgsql.put(JDBCType.DOUBLE, "float8");
    JDBCToPgsql.put(JDBCType.VARCHAR, "text");
    JDBCToPgsql.put(JDBCType.DATE, "date");
    JDBCToPgsql.put(JDBCType.TIME, "time");
    JDBCToPgsql.put(JDBCType.TIMESTAMP, "timestamp");
  }

  static {
    javaTypeToJDBC.put(short.class, JDBCType.INTEGER);
    javaTypeToJDBC.put(int.class, JDBCType.INTEGER);
    javaTypeToJDBC.put(long.class, JDBCType.BIGINT);
    javaTypeToJDBC.put(float.class, JDBCType.FLOAT);
    javaTypeToJDBC.put(double.class, JDBCType.DOUBLE);
    javaTypeToJDBC.put(boolean.class, JDBCType.BOOLEAN);
    javaTypeToJDBC.put(byte[].class, JDBCType.BINARY);
    javaTypeToJDBC.put(Short.class, JDBCType.SMALLINT);
    javaTypeToJDBC.put(Integer.class, JDBCType.INTEGER);
    javaTypeToJDBC.put(Long.class, JDBCType.BIGINT);
    javaTypeToJDBC.put(Float.class, JDBCType.FLOAT);
    javaTypeToJDBC.put(Double.class, JDBCType.DOUBLE);
    javaTypeToJDBC.put(String.class, JDBCType.VARCHAR);
    javaTypeToJDBC.put(LocalDateTime.class, JDBCType.TIMESTAMP);
    javaTypeToJDBC.put(LocalDate.class, JDBCType.DATE);
    javaTypeToJDBC.put(Boolean.class, JDBCType.BOOLEAN);
    javaTypeToJDBC.put(Enum.class, JDBCType.VARCHAR);
  }

  public static JDBCType getJDBCType(Class<?> javaType) {
    if (Enum.class.isAssignableFrom(javaType)) {
      return javaTypeToJDBC.get(Enum.class);
    }
    if (javaTypeToJDBC.containsKey(javaType)) {
      return javaTypeToJDBC.get(javaType);
    }
    throw new IllegalArgumentException("Can't find JDBC equivalent for java type [" + javaType.getSimpleName() + "]");
  }

  public static String getPgsqlName(JDBCType jdbcType) {
    if (JDBCToPgsql.containsKey(jdbcType)) {
      return JDBCToPgsql.get(jdbcType);
    }
    throw new IllegalArgumentException("Can't find Postgres type name for JDBC type [" + jdbcType.name() + "]");
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