package ru.moleus.kollector.feature.main

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.feature.overview.root.Overview
import ru.moleus.kollector.feature.auth.Authentication
import ru.moleus.kollector.feature.builder.integration.BuilderComponent
import ru.moleus.kollector.feature.map.EntitiesMap

interface Main {
    val routerState: Value<RouterState<*, Child>>
    val model : Value<Model>

    fun onTabClick(tab: Tab)

    data class Model(
        val selectedTab: Tab = Tab.TABLE,
    )

    enum class Tab {
        TABLE, REGISTRATION, MAP, ADD_ENTITY
    }

    sealed interface Child {
        class OverviewScreen(val component: Overview) : Child
        class RegistrationScreen(val component: Authentication) : Child
        class MapScreen(val component: EntitiesMap) : Child
        class AddEntityScreen(val component: BuilderComponent) : Child
    }
}