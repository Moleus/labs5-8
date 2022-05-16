package overview.table

import com.arkivanov.decompose.value.Value
import common.`entities-overview`.overview.table.store.TableStore

interface EntitiesTable {
    val store: Value<TableStore>

    fun onEntityClicked(id: Long)
}