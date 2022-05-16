package overview.ui.table.lazy.model

import androidx.compose.ui.unit.Dp

interface Attribute<T> {
    val label: String
    val value: T
    val tableColumnWidth: Dp

    fun getValueAsText(): String
}