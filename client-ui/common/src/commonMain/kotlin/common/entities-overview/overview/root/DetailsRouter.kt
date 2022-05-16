package common.`entities-overview`.overview.root

import data.EntityProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable
import overview.details.EntityDetails
import overview.details.EntityDetailsComponent
import overview.root.Overview.DetailsChild

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

class DetailsRouter(
    componentContext: ComponentContext,
    private val entityProvider: EntityProvider,
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
        }

    private fun entityDetails(componentContext: ComponentContext, entityId: Long): EntityDetails =
        EntityDetailsComponent(
            componentContext = componentContext,
            entityProvider = entityProvider,
            entityId = entityId,
            isToolbarVisible = isToolbarVisible,
            onComplete = onFinished
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

    fun isShown(): Boolean =
        when (router.activeChild.configuration) {
            is Config.None -> false
            is Config.Details -> true
        }

    sealed interface Config : Parcelable {
        @Parcelize
        object None : Config

        @Parcelize
        data class Details(val entityId: Long) : Config
    }
}
