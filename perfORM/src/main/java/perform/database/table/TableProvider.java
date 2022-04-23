package perform.database.table;

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
public class TableProvider {
  private final EntityProperty entityProperty;
  @SuppressWarnings("FieldCanBeLocal")
  private final String tableName;
  private final RelationalTable entityTable;

  private RelationalColumn primaryColumn;
  private final List<String> usedColumnNames = new ArrayList<>();

  public TableProvider(EntityProperty entityProperty) {
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

  private List<RelationalColumn> createColumnsFromEntity(EntityProperty entityProperty, String namePrefix) {
    List<RelationalColumn> columns = new ArrayList<>();
    for (FieldProperty field : entityProperty.getProperties()) {
      if (field.isEmbedded()) {
        String nextPrefix = namePrefix + field.getEmbeddedPrefix();
        EntityProperty<?> embeddedEntity = entityProperty.getEmbeddedBy(field);
        createColumnsFromEntity(embeddedEntity, nextPrefix);
        continue;
      }
      RelationalColumn column = createColumn(field, namePrefix);
      checkDuplicates(column);
      columns.add(column);
    }
    return columns;
  }

  private void checkDuplicates(RelationalColumn column) {
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

  private RelationalColumn createColumn(FieldProperty field, String namePrefix) {
    ColumnCreator columnCreator = new ColumnCreator(field, namePrefix);
    return columnCreator.createColumn();
  }
}