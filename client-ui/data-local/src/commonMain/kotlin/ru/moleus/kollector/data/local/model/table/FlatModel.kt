package ru.moleus.kollector.data.local.model.table

import model.data.Flat

class FlatModel(flat: Flat) : TableModel {
    override val id = DefaultAttribute(label = "Id", value = flat.id, tableColumnWidth = 100)
    private val name = DefaultAttribute(label = "Name", value = flat.name, tableColumnWidth = 200)
    private val area = DefaultAttribute(label = "Area", value = flat.area, tableColumnWidth = 200)
    private val rooms = DefaultAttribute(label = "Rooms", value = flat.numberOfRooms, tableColumnWidth = 200)

    override val displayedAttributesInTable: List<Attribute<*>> = listOf(
        id,
        name,
        area,
        rooms
    )
}