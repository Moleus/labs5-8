package ru.moleus.kollector.data.local.model.table

import model.data.Flat
import ru.moleus.kollector.data.local.model.table.HeaderLabel.*

class FlatModel(flat: Flat) : TableModel {
    override val id = DefaultAttribute(label = ID, value = flat.id, tableColumnWidth = 50)
    private val name = DefaultAttribute(label = NAME, value = flat.name, tableColumnWidth = 150)
    private val cordX = DefaultAttribute(label = CORD_X, value = flat.coordinates?.x, tableColumnWidth = 80)
    private val cordY = DefaultAttribute(label = CORD_Y, value = flat.coordinates?.y, tableColumnWidth = 80)
    private val creationDate =
        DefaultAttribute(label = CREATION_DATE, value = flat.creationDate, tableColumnWidth = 150)
    private val area = DefaultAttribute(label = AREA, value = flat.area, tableColumnWidth = 50)
    private val numberOfRooms = DefaultAttribute(label = ROOMS, value = flat.numberOfRooms, tableColumnWidth = 80)
    private val furniture = DefaultAttribute(label = FURNITURE, value = flat.furniture, tableColumnWidth = 100)
    private val newness = DefaultAttribute(label = NEWNESS, value = flat.newness, tableColumnWidth = 100)
    private val view = DefaultAttribute(label = VIEW, value = flat.view, tableColumnWidth = 75)
    private val houseName = DefaultAttribute(label = HOUSE_NAME, value = flat.house?.name, tableColumnWidth = 120)
    private val houseYear = DefaultAttribute(label = HOUSE_YEAR, value = flat.house?.year, tableColumnWidth = 120)
    private val houseFloors =
        DefaultAttribute(label = HOUSE_FLOORS, value = flat.house?.numberOfFloors, tableColumnWidth = 120)
    private val houseLifts =
        DefaultAttribute(label = HOUSE_LIFT, value = flat.house?.numberOfLifts, tableColumnWidth = 120)

    override val displayedAttributesInTable: List<Attribute<*>> = listOf(
        id,
        name,
        cordX,
        cordY,
        creationDate,
        area,
        numberOfRooms,
        furniture,
        newness,
        view,
        houseName,
        houseYear,
        houseFloors,
        houseLifts
    )
}