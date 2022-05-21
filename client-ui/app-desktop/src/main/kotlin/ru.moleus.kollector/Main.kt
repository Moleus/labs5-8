package ru.moleus.kollector

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import common.root.ui.RootUi
import ru.moleus.kollector.domain.bootsrap.ClientBootstrapper
import ru.moleus.kollector.feature.root.RootComponent
import ru.moleus.kollector.ui.compose.theme.MyTheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
fun main() {
    val clientContext = ClientBootstrapper.initClientContext()

    val lifecycle = LifecycleRegistry()
    val root =
        RootComponent(
            componentContext = DefaultComponentContext(lifecycle),
            clientContext = clientContext
        )

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Decompose Dynamic Features"
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MyTheme {
                    RootUi(root = root)
                }
            }
        }
    }
}
