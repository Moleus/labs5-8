package ru.moleus.kollector.data.local.model.table

interface TableModel {
    val id : Attribute<Long>
    val displayedAttributesInTable : List<Attribute<*>>
}