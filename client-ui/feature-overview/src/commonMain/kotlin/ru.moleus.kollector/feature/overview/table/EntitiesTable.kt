package ru.moleus.kollector.feature.overview.table

import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.data.local.model.table.TableModel

interface EntitiesTable {
    val model: Value<Model>

    fun onEntityClicked(id: Long)

    data class Model(
        val entityModels: List<TableModel>,
        val defaultModel: TableModel,
        val selectedEntityId: Long?,
        val isLoading: Boolean = false,
    )
}