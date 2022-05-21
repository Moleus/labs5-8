package ru.moleus.kollector.ui.compose.main

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.*
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.moleus.kollector.ui.compose.overview.OverviewUi
import ru.moleus.kollector.ui.compose.main.ui.BottomNavBar
import registration.ui.RegistrationUi
import ru.moleus.kollector.feature.main.Main
import ru.moleus.kollector.feature.main.Main.*
import ru.moleus.kollector.ui.compose.MapUi
import ru.moleus.kollector.ui.compose.builder.BuilderUi

private val SMARTPHONE_WIDTH_THRESHOLD = 400.dp

@ExperimentalDecomposeApi
@ExperimentalComposeUiApi
@Composable
fun MainUi(component: Main, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    BoxWithConstraints {
        val isDesktopLayout = this@BoxWithConstraints.maxWidth >= SMARTPHONE_WIDTH_THRESHOLD
        Scaffold(
            bottomBar = {
                if (!isDesktopLayout)
                    BottomNavBar(
                        selectedTab = model.selectedTab,
                        onClick = component::onTabClick
                    )
            }
        ) {
            Row(Modifier.padding(it)) {
                if (isDesktopLayout) {
                    NavBar(
                        selectedTab = model.selectedTab,
                        onClick = component::onTabClick
                    )
                }
                Children(
                    routerState = component.routerState,
                    animation = childAnimation(fade() + scale())
                ) {
                    when (val child = it.instance) {
                        is Child.OverviewScreen -> OverviewUi(child.component, modifier = modifier)
                        is Child.RegistrationScreen -> RegistrationUi(child.component, modifier = modifier)
                        is Child.MapScreen -> MapUi(child.component, modifier = modifier)
                        is Child.AddEntityScreen -> BuilderUi(child.component, modifier = modifier)
                    }
                }
            }
        }

    }
}