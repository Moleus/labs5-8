package ru.moleus.kollector.data.local.model.table

data class DefaultAttribute<T>(
    override val label: HeaderLabel,
    override val value: T,
    override val tableColumnWidth: Int
) : Attribute<T> {
    override fun getValueAsText(): String = value?.toString().orEmpty()
}