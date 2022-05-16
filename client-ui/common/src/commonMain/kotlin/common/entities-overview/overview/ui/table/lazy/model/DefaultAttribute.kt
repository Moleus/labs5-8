package overview.ui.table.lazy.model

import androidx.compose.ui.unit.Dp

data class DefaultAttribute<T>(
    override val label: String,
    override val value: T,
    override val tableColumnWidth: Dp
) : Attribute<T> {
    override fun getValueAsText(): String = value?.toString().orEmpty()
}