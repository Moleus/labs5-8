package ru.moleus.kollector.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.scale
import ru.moleus.kollector.feature.root.Root
import ru.moleus.kollector.ui.compose.connection.ConnectionUi
import ru.moleus.kollector.ui.compose.main.MainUi

@ExperimentalComposeUiApi
@ExperimentalDecomposeApi
@Composable
fun RootUi(root: Root) {
    Children(
        routerState = root.routerState,
        animation = childAnimation(fade() + scale())
    ) {
        when (val child = it.instance) {
            is Root.Child.Connection -> ConnectionUi(child.component, modifier = Modifier.fillMaxSize())
            is Root.Child.MainChild -> MainUi(child.component, modifier = Modifier.fillMaxSize())
        }
    }
}
