package overview.details

import com.arkivanov.decompose.value.Value
import overview.ui.table.lazy.model.TableModel

interface EntityDetails {
    val model: Value<Model>

    fun onCloseClicked()

    data class Model(
        val isToolbarVisible: Boolean,
        val entityModel: TableModel
    )
}