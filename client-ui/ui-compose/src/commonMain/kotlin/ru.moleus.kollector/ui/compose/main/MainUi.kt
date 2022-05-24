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
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.moleus.kollector.feature.main.Main
import ru.moleus.kollector.feature.main.Main.Child
import ru.moleus.kollector.ui.compose.auth.RegistrationUi
import ru.moleus.kollector.ui.compose.builder.CreatorUi
import ru.moleus.kollector.ui.compose.main.ui.BottomNavBar
import ru.moleus.kollector.ui.compose.map.MapUi
import ru.moleus.kollector.ui.compose.overview.OverviewUi

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
                        is Child.OverviewScreen -> OverviewUi(child.component)
                        is Child.RegistrationScreen -> RegistrationUi(child.component, modifier = modifier)
                        is Child.MapScreen -> MapUi(child.component, modifier = modifier)
                        is Child.AddEntityScreen -> CreatorUi(child.component, modifier = modifier)
                    }
                }
            }
        }

    }
}