package ru.moleus.kollector.feature.overview.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.observable.Observable
import common.context.EntityProvider
import ru.moleus.kollector.feature.overview.table.EntitiesTable
import ru.moleus.kollector.feature.overview.table.integration.EntitiesTableComponent

internal class TableRouter(
    componentContext: ComponentContext,
    private val entityProvider: EntityProvider,
    private val selectedEntityId: Observable<Long?>,
    private val onEntitySelected: (id: Long) -> Unit
)  {

    private val router =
        componentContext.router<Config, Overview.TableChild>(
            initialConfiguration = Config.Table,
            key = "TableRouter",
            childFactory = ::createChild
        )

    val state: Value<RouterState<Config, Overview.TableChild>> = router.state

    private fun createChild(config: Config, componentContext: ComponentContext): Overview.TableChild =
        when (config) {
            is Config.Table -> Overview.TableChild.Table(EntitiesTable(componentContext))
            is Config.None -> Overview.TableChild.None
        }

    private fun EntitiesTable(componentContext: ComponentContext): EntitiesTable =
        EntitiesTableComponent(
            componentContext = componentContext,
            entityProvider = entityProvider,
            selectedEntityId = selectedEntityId,
            onEntitySelected = onEntitySelected
        )

    fun moveToBackStack() {
        if (router.activeChild.configuration !is Config.None) {
            router.push(Config.None)
        }
    }

    fun show() {
        if (router.activeChild.configuration !is Config.Table) {
            router.pop()
        }
    }

    sealed interface Config : Parcelable {
        @Parcelize
        object Table : Config

        @Parcelize
        object None : Config
    }
}
