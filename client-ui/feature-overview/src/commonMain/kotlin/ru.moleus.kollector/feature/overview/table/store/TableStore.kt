package ru.moleus.kollector.feature.overview.table.store

import ru.moleus.kollector.data.local.model.table.TableModel


data class TableStore(
    val entityModels: List<TableModel>,
    val defaultModel: TableModel,
    val selectedEntityId: Long?,
    val isLoading: Boolean = false,
)
