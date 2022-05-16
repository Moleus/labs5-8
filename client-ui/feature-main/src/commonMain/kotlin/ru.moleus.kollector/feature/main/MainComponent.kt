package ru.moleus.kollector.feature.main

import ClientContext
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ru.moleus.kollector.feature.main.Main.*
import editor.Editor
import common.`entities-overview`.editor.integration.BuilderComponent
import overview.root.Overview
import overview.root.OverviewComponent
import registration.form.Authentication
import registration.form.integration.AuthComponent
import root.MapComponent
import root.EntitiesMap

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
            authenticator = clientContext
        )

    private fun mapScreen(componentContext: ComponentContext): EntitiesMap =
        MapComponent(
            componentContext = componentContext,
            entityProvider = clientContext
        )

    private fun newEntityScreen(componentContext: ComponentContext): Editor =
        BuilderComponent(
            componentContext = componentContext,
            dtoBuilder = clientContext,
            onClose = { router.pop() },
            onSubmit = { router.pop() }
        )

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