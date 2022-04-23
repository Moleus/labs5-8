package server.generated;

import model.data.Flat;
import perform.database.PreparedStatementProvider;
import perform.database.query.Statements;
import perform.exception.PerformException;
import perform.mapping.mappers.EntityRowMapper;
import perform.mapping.mappers.EntityToRowMapper;
import perform.util.PreparedStatementUtil;
import server.generated.repository.FlatRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlatRepositoryImpl implements FlatRepository {
  private final PreparedStatementProvider psProvider;
  private final EntityRowMapper<Flat> fromRowMapper;
  private final EntityToRowMapper<Flat> fromEntityMapper;
  private final Statements statements;

  public FlatRepositoryImpl(PreparedStatementProvider preparedStatementProvider, EntityRowMapper<Flat> fromRowMapper, EntityToRowMapper<Flat> fromEntityMapper, Statements statements) {
    this.psProvider = preparedStatementProvider;
    this.fromRowMapper = fromRowMapper;
    this.fromEntityMapper = fromEntityMapper;
    this.statements = statements;
  }

  @Override
  public Flat findById(long id) {
    String idColumnName = "id";
    try (PreparedStatement ps = psProvider.prepareStatement(statements.selectBy(idColumnName))) {
      ps.setLong(1, id);
      ResultSet resultSet = ps.executeQuery();
      existsOrThrow(resultSet);
      return fromRowMapper.mapRow(resultSet);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new PerformException("Failed to find Flat by id: " + e.getMessage());
    }
  }

  @Override
  public List<Flat> findAll() {
    List<Flat> entities = new ArrayList<>();
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

  @Override
  public long save(Flat entity) {
    PreparedStatement ps = psProvider.prepareStatement(statements.insert());
    int affectedRows = fromEntityMapper.mapEntity(entity, ps);
    if (affectedRows == 0) {
      throw new PerformException("Creating Flat failed, no rows affected");
    }
    // TODO: move getGenerated to Utils/helper class
    return PreparedStatementUtil.getGeneratedId(ps);
  }

  @Override
  public boolean update(Flat entity) {
    return false;
  }

  @Override
  public void delete(Flat entity) {
    String idColumnName = "id";
    try (PreparedStatement ps = psProvider.prepareStatement(statements.deleteBy(idColumnName))) {
      ps.setLong(1, entity.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new PerformException("Failed to delete Flat by id: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    try (PreparedStatement ps = psProvider.prepareStatement(statements.deleteAll())) {
      ps.execute();
    } catch (SQLException e) {
      throw new PerformException("Failed to delete all Flats from table: " + e.getMessage());
    }
  }

  private static void existsOrThrow(ResultSet resultSet) throws SQLException {
    if (!resultSet.next()) {
      throw new SQLException();
    }
  }
}