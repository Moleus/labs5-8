package overview.ui.table.lazy.model

import androidx.compose.ui.unit.dp
import data.Flat

class FlatModel(flat: Flat) : TableModel {
    override val id = DefaultAttribute(label = "Id", value = flat.id, tableColumnWidth = 100.dp)
    val name = DefaultAttribute(label = "Name", value = flat.name, tableColumnWidth = 200.dp)
    val age = DefaultAttribute(label = "Age", value = flat.age, tableColumnWidth = 200.dp)
    val rooms = DefaultAttribute(label = "Rooms", value = flat.rooms, tableColumnWidth = 200.dp)

    override val displayedAttributesInTable: List<Attribute<*>> = listOf(
        id,
        name,
        age,
        rooms
    )
}