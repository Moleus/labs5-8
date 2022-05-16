package overview.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import overview.details.EntityDetails
import overview.table.EntitiesTable

interface Overview {
    val model: Value<Model>
    val tableRouterState: Value<RouterState<*, TableChild>>
    val detailsRouterState: Value<RouterState<*, DetailsChild>>

    fun setMultiPane(isMultiPane: Boolean)

    data class Model(
        val isMultiPane: Boolean = false
    )

    sealed class TableChild {
        class Table(val component: EntitiesTable) : TableChild()
        object None : TableChild()
    }

    sealed class DetailsChild {
        object None : DetailsChild()
        data class Details(val component: EntityDetails) : DetailsChild()
    }
}