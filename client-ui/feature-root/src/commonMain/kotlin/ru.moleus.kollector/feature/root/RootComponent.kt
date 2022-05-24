package ru.moleus.kollector.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import common.context.ClientContext
import common.context.Session
import ru.moleus.kollector.feature.connection.Connection
import ru.moleus.kollector.feature.connection.integration.ConnectionComponent
import ru.moleus.kollector.feature.main.Main
import ru.moleus.kollector.feature.main.MainComponent

class RootComponent(
    componentContext: ComponentContext,
    private val clientSession: Session,
    private val getContext: (Session) -> ClientContext
) : Root, ComponentContext by componentContext {
    private lateinit var clientContext: ClientContext
    private val router =
        router<Config, Root.Child>(
            initialConfiguration = Config.Connection,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Root.Child>> = router.state


    private fun createChild(config: Config, componentContext: ComponentContext): Root.Child =
        when (config) {
            is Config.Connection -> Root.Child.Connection(connection(componentContext))
            is Config.Main -> Root.Child.MainChild(main(componentContext))
            else -> Root.Child.Connection(connection(componentContext))
        }

    private fun main(componentContext: ComponentContext): Main =
        MainComponent(componentContext, clientContext)

    private fun connection(componentContext: ComponentContext): Connection =
        ConnectionComponent(componentContext, clientSession, getContext, ::onConnected)

    private fun onConnected(clientContext: ClientContext) {
        this.clientContext = clientContext
        router.replaceCurrent(Config.Main)
    }

    interface Config : Parcelable {
        @Parcelize
        object Main : Config

        @Parcelize
        object Connection : Config
    }
}