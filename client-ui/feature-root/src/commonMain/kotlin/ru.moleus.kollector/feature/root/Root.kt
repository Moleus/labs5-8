package ru.moleus.kollector.feature.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.feature.main.Main

interface Root {
    val routerState: Value<RouterState<*, Child>>

    sealed interface Child {
        class MainChild(val component: Main) : Child
        class Connection(val component: ru.moleus.kollector.feature.connection.Connection) : Child
    }
}