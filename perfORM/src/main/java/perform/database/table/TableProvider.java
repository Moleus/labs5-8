package perform.database.table;

import org.apache.commons.text.CaseUtils;
import perform.database.table.column.ColumnCreator;
import perform.database.table.column.RelationalColumn;
import perform.exception.DuplicateKeyException;
import perform.mapping.properties.EntityProperty;
import perform.mapping.properties.FieldProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a {@link RelationalTable} by an {@link EntityProperty}.
 */
public class TableProvider<T> {
  private final EntityProperty<T> entityProperty;
  @SuppressWarnings("FieldCanBeLocal")
  private final String tableName;
  private final RelationalTable entityTable;

  private RelationalColumn<?> primaryColumn;
  private final List<String> usedColumnNames = new ArrayList<>();

  public TableProvider(EntityProperty<T> entityProperty) {
    this.entityProperty = entityProperty;
    this.tableName = entityProperty.getTableName();
    this.entityTable = new RelationalTable(tableName);
    fillTableWithColumns();
  }

  public RelationalTable getTable() {
    return entityTable;
  }

  private void fillTableWithColumns() {
    entityTable.setColumns(createColumnsFromEntity(entityProperty, ""));
  }

  private List<RelationalColumn<?>> createColumnsFromEntity(EntityProperty<?> entityProperty, String namePrefix) {
    List<RelationalColumn<?>> columns = new ArrayList<>();
    for (FieldProperty<?> field : entityProperty.getProperties()) {
      if (field.isEmbedded()) {
        String nextPrefix = CaseUtils.toCamelCase(namePrefix + " " + field.getEmbeddedPrefix(), false);
        EntityProperty<?> embeddedEntity = entityProperty.getEmbeddedBy(field);
        columns.addAll(createColumnsFromEntity(embeddedEntity, nextPrefix));
        continue;
      }
      RelationalColumn<?> column = createColumn(field, namePrefix);
      checkDuplicates(column);
      columns.add(column);
    }
    return columns;
  }

  private void checkDuplicates(RelationalColumn<?> column) {
    if (column.isId()) {
      if (primaryColumn != null) {
        throw new DuplicateKeyException(entityProperty.getType(), "Id");
      }
      primaryColumn = column;
    }
    String columnName = column.getColumnName();
    if (usedColumnNames.contains(columnName)) {
      throw new DuplicateKeyException(entityProperty.getType(), "Column Name (" + columnName + ")");
    }
    usedColumnNames.add(columnName);
  }

  private <F> RelationalColumn<F> createColumn(FieldProperty<F> field, String namePrefix) {
    ColumnCreator<F> columnCreator = new ColumnCreator<>(field, namePrefix);
    this.entityTable.setDependsOnTable(columnCreator.getColumnForeignTableName());
    return columnCreator.createColumn();
  }
}