package ru.moleus.kollector.feature.overview.table

import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.feature.overview.table.store.TableStore

interface EntitiesTable {
    val store: Value<TableStore>

    fun onEntityClicked(id: Long)
}