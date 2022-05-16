package overview.ui.table.lazy.model

interface TableModel {
    val id : Attribute<Long>
    val displayedAttributesInTable : List<Attribute<*>>
}