package perform.database.repository;

import perform.database.PreparedStatementProvider;
import perform.database.query.Statements;
import perform.exception.PerformException;
import perform.mapping.mappers.EntityRowMapper;
import perform.mapping.mappers.EntityToRowMapper;
import perform.util.PreparedStatementUtil;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenericRepositoryOperations<T> {
  private final PreparedStatementProvider psProvider;
  private final Statements statements;
  private final EntityRowMapper<T> fromRowMapper;
  private final EntityToRowMapper<T> fromEntityMapper;

  public GenericRepositoryOperations(
      PreparedStatementProvider psProvider,
      Statements statements,
      EntityRowMapper<T> fromRowMapper,
      EntityToRowMapper<T> fromEntityMapper) {
    this.psProvider = psProvider;
    this.statements = statements;
    this.fromRowMapper = fromRowMapper;
    this.fromEntityMapper = fromEntityMapper;
  }

  public T genericFindBy(Object value, String columnName) {
    try (PreparedStatement ps = psProvider.prepareStatement(statements.selectBy(columnName))) {
      setValue(ps, 1, value);
      ResultSet resultSet = ps.executeQuery();
      existsOrThrow(resultSet);
      return fromRowMapper.mapRow(resultSet);
    } catch (SQLException e) {
      throw new PerformException("Failed to find Entity by [" + columnName + "]", e);
    }
  }

  public boolean genericUpdate(T entity, String columnName, Object value) {
    try (PreparedStatement ps = psProvider.prepareStatement(statements.updateBy(columnName))) {
      int nextIndex = fromEntityMapper.mapEntity(entity, ps);
      setValue(ps, nextIndex, value);
      return executeUpdate(ps);
    } catch (SQLException e) {
      throw new PerformException("Failed to update Entity by [" + columnName + "]", e);
    }
  }

  public List<T> findAll() {
    List<T> entities = new ArrayList<>();
    try (PreparedStatement ps = psProvider.prepareStatement(statements.selectAll())) {
      ResultSet resultSet = ps.executeQuery();
      while (resultSet.next()) {
        entities.add(fromRowMapper.mapRow(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new PerformException("Failed to get all Entities from table: " + e.getMessage());
    }
    return entities;
  }

  public void delete(T entity, String columnName, Object value) {
    try (PreparedStatement ps = psProvider.prepareStatement(statements.deleteBy(columnName))) {
      setValue(ps, 1, value);
      ps.execute();
    } catch (SQLException e) {
      throw new PerformException("Failed to delete [" + entity + "] by column [" + columnName + "]" + e.getMessage());
    }
  }

  public long save(T entity) {
    PreparedStatement ps = psProvider.prepareStatement(statements.insert());
    fromEntityMapper.mapEntity(entity, ps);
    if (!executeUpdate(ps)) {
      throw new PerformException("Failed to save an entity [" + entity.getClass().getSimpleName() + "]. No rows affected.");
    }
    return PreparedStatementUtil.getGeneratedId(ps);
  }

  private boolean executeUpdate(PreparedStatement ps) {
    try {
      return ps.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new PerformException("Entity update failed");
    }
  }

  private void setValue(PreparedStatement ps, int index, Object value) throws SQLException {
    JDBCType jdbcType = PreparedStatementUtil.getSqlType(value.getClass());
    PreparedStatementUtil.setValue(ps, index, jdbcType, value);
  }

  private static void existsOrThrow(ResultSet resultSet) throws SQLException {
    if (!resultSet.next()) {
      throw new SQLException();
    }
  }
}
