package common.`entities-overview`.overview.table.store

import overview.ui.table.lazy.model.TableModel

data class TableStore(
    val entityModels: List<TableModel>,
    val defaultModel: TableModel,
    val selectedEntityId: Long?,
    val isLoading: Boolean = false,
)
