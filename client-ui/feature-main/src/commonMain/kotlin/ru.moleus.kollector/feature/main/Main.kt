package ru.moleus.kollector.feature.main

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import editor.Editor
import overview.root.Overview
import registration.form.Authentication
import root.EntitiesMap

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
        class AddEntityScreen(val component: Editor) : Child
    }
}