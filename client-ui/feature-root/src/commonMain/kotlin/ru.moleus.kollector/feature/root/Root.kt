package ru.moleus.kollector.feature.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.feature.main.Main

interface Root {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        class MainChild(val component: Main) : Child()
    }
}