package ru.moleus.kollector.feature.overview.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.feature.builder.integration.BuilderComponent
import ru.moleus.kollector.feature.builder.integration.UpdaterComponent
import ru.moleus.kollector.feature.overview.details.EntityDetails
import ru.moleus.kollector.feature.overview.table.EntitiesTable

interface Overview {
    val model: Value<Model>
    val tableRouterState: Value<RouterState<*, TableChild>>
    val detailsRouterState: Value<RouterState<*, DetailsChild>>

    fun setMultiPane(isMultiPane: Boolean)

    data class Model(
        val isMultiPane: Boolean = false
    )

    sealed interface TableChild {
        class Table(val component: EntitiesTable) : TableChild
        object None : TableChild
    }

    sealed interface DetailsChild {
        object None : DetailsChild
        data class Details(val component: EntityDetails) : DetailsChild
        data class Updater(val component: UpdaterComponent) : DetailsChild
    }
}