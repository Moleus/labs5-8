package ru.moleus.kollector.feature.main

import common.context.ClientContext
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.moleus.kollector.domain.client.LocalCommandExecutor
import ru.moleus.kollector.domain.client.RemoteCommandExecutor
import ru.moleus.kollector.feature.main.Main.*
import ru.moleus.kollector.feature.overview.root.Overview
import ru.moleus.kollector.feature.overview.root.OverviewComponent
import ru.moleus.kollector.feature.auth.Authentication
import ru.moleus.kollector.feature.auth.integration.AuthComponent
import ru.moleus.kollector.feature.builder.Builder
import ru.moleus.kollector.feature.builder.integration.BuilderComponent
import ru.moleus.kollector.feature.map.EntitiesMap
import ru.moleus.kollector.feature.map.integration.MapComponent

class MainComponent(
    componentContext: ComponentContext,
    private val clientContext: ClientContext
) : Main, ComponentContext by componentContext {
    private val router =
        router<Config, Child>(
            initialConfiguration = Config.EntitiesOverview,
            key = "DetailsRouter",
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Child>> = router.state

//    private val drawerState = BehaviorSubject(DrawerState(DrawerValue.Closed))

//    override fun onDrawerClose() {
//        drawerState.onNext(drawerState)
//    }

    override val model: Value<Model> = router.state.map { state ->
        Model(
            selectedTab = state.activeChild.configuration.toTab()
        )
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.EntitiesOverview -> Child.OverviewScreen(overviewScreen(componentContext))
            is Config.Registration -> Child.RegistrationScreen(registrationScreen(componentContext))
            is Config.Map -> Child.MapScreen(mapScreen(componentContext))
            is Config.AddEntity -> Child.AddEntityScreen(newEntityScreen(componentContext))
        }


    private fun overviewScreen(componentContext: ComponentContext): Overview =
        OverviewComponent(
            componentContext = componentContext,
            clientContext = clientContext,
        )

    private fun registrationScreen(componentContext: ComponentContext): Authentication =
        AuthComponent(
            componentContext = componentContext,
            commandExecutor = LocalCommandExecutor(clientContext)
        )

    private fun mapScreen(componentContext: ComponentContext): EntitiesMap =
        MapComponent(
            componentContext = componentContext,
            entityProvider = clientContext
        )

    private fun newEntityScreen(componentContext: ComponentContext): BuilderComponent =
        BuilderComponent(
            componentContext = componentContext,
            dtoBuilder = clientContext,
            commandExecutor = RemoteCommandExecutor(clientContext)
        ) { router.pop() }

    override fun onTabClick(tab: Tab): Unit =
        when (tab) {
            Tab.TABLE -> router.replaceCurrent(Config.EntitiesOverview)
            Tab.REGISTRATION -> router.replaceCurrent(Config.Registration)
            Tab.MAP -> router.replaceCurrent(Config.Map)
            Tab.ADD_ENTITY -> router.replaceCurrent(Config.AddEntity)
        }

    private fun Config.toTab(): Tab =
        when (this) {
            is Config.EntitiesOverview -> Tab.TABLE
            is Config.Registration -> Tab.REGISTRATION
            is Config.Map -> Tab.MAP
            Config.AddEntity -> Tab.ADD_ENTITY
        }

    private sealed interface Config : Parcelable {
        @Parcelize
        object EntitiesOverview : Config

        @Parcelize
        object Registration : Config

        @Parcelize
        object Map : Config

        @Parcelize
        object AddEntity : Config
    }
}