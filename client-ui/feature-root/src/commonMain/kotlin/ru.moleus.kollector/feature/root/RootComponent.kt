package ru.moleus.kollector.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.decompose.router.router
import common.context.ClientContext
import ru.moleus.kollector.feature.main.Main
import ru.moleus.kollector.feature.main.MainComponent

class RootComponent(
    componentContext: ComponentContext,
    private val clientContext: ClientContext
) : Root, ComponentContext by componentContext {
    private val router =
        router<Config, Root.Child>(
            initialConfiguration = Config.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Root.Child>> = router.state

    private fun createChild(config: Config, componentContext: ComponentContext): Root.Child =
        when (config) {
            is Config.Main -> Root.Child.MainChild(main(componentContext))
            else -> Root.Child.MainChild(main(componentContext))
        }

    private fun main(componentContext: ComponentContext): Main =
        MainComponent(componentContext, clientContext)

    interface Config : Parcelable {
        @Parcelize
        object Main : Config
    }
}