package ru.moleus.kollector.feature.overview.details

import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.data.local.model.table.TableModel

interface EntityDetails {
    val model: Value<Model>

    fun onCloseClicked()

    fun onUpdateClicked(entityId: Long)

    data class Model(
        val isToolbarVisible: Boolean,
        val entityModel: TableModel
    )
}