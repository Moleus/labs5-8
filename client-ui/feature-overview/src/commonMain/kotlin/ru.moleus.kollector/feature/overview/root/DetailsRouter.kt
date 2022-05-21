package ru.moleus.kollector.feature.overview.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable
import ru.moleus.kollector.feature.overview.details.EntityDetails
import ru.moleus.kollector.feature.overview.details.EntityDetailsComponent
import ru.moleus.kollector.feature.overview.root.Overview.DetailsChild

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import common.context.ClientContext
import ru.moleus.kollector.feature.builder.integration.UpdaterComponent

class DetailsRouter(
    componentContext: ComponentContext,
    private val clientContext: ClientContext,
    private val isToolbarVisible: Observable<Boolean>,
    private val onFinished: () -> Unit
) {
    private val router =
        componentContext.router<Config, DetailsChild>(
            initialConfiguration = Config.None,
            key = "DetailsRouter",
            childFactory = ::createChild
        )

    val state: Value<RouterState<Config, DetailsChild>> = router.state

    private fun createChild(config: Config, componentContext: ComponentContext): DetailsChild =
        when (config) {
            is Config.None -> DetailsChild.None
            is Config.Details -> DetailsChild.Details(
                entityDetails(
                    componentContext = componentContext,
                    entityId = config.entityId
                )
            )
            is Config.Updater -> DetailsChild.Updater(
               UpdaterComponent(
                  componentContext = componentContext,
                   dtoBuilder = clientContext,
                   exchanger = clientContext,
                   entityId = config.entityId,
                   onClose = ::closeUpdater
               )
            )
        }

    private fun entityDetails(componentContext: ComponentContext, entityId: Long): EntityDetails =
        EntityDetailsComponent(
            componentContext = componentContext,
            entityProvider = clientContext,
            entityId = entityId,
            isToolbarVisible = isToolbarVisible,
            onComplete = onFinished,
            onUpdateButtonClicked = ::showUpdater
        )

    fun showEntity(id: Long) {
        router.navigate { stack ->
            stack
                .dropLastWhile { it is Config.Details }
                .plus(Config.Details(entityId = id))
        }
    }

    fun closeEntity() {
        router.popWhile { it !is Config.None }
    }

    private fun showUpdater(id: Long) {
        router.navigate {stack ->
            stack.dropLastWhile { it is Config.Updater }
                .plus(Config.Updater(entityId = id))
        }
    }

    fun closeUpdater() {
        router.popWhile { it !is Config.Details }
    }

    fun isShown(): Boolean =
        when (router.activeChild.configuration) {
            is Config.None -> false
            is Config.Details -> true
            is Config.Updater -> true
        }

    sealed interface Config : Parcelable {
        @Parcelize
        object None : Config

        @Parcelize
        data class Details(val entityId: Long) : Config

        @Parcelize
        data class Updater(val entityId: Long) : Config
    }
}
