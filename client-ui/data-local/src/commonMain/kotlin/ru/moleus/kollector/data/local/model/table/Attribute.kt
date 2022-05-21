package ru.moleus.kollector.data.local.model.table

interface Attribute<T> {
    val label: String
    val value: T
    val tableColumnWidth: Int

    fun getValueAsText(): String
}