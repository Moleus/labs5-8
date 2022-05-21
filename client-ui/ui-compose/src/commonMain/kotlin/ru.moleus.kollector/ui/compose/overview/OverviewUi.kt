package ru.moleus.kollector.ui.compose.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ru.moleus.kollector.ui.compose.overview.table.TableUi
import ru.moleus.kollector.feature.overview.root.Overview
import ru.moleus.kollector.ui.compose.builder.BuilderUi

private val MULTI_PANE_WIDTH_THRESHOLD = 800.dp
private const val LIST_PANE_WEIGHT = 0.4F
private const val DETAILS_PANE_WEIGHT = 0.6F

@Composable
fun OverviewUi(component: Overview, modifier: Modifier = Modifier) {
    BoxWithConstraints {
        val model by component.model.subscribeAsState()
        val isMultiPane = model.isMultiPane

        val isMultiPaneRequired = this@BoxWithConstraints.maxWidth >= MULTI_PANE_WIDTH_THRESHOLD

        DisposableEffect(isMultiPaneRequired) {
            component.setMultiPane(isMultiPaneRequired)
            onDispose {}
        }

        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(if (isMultiPane) LIST_PANE_WEIGHT else 1F)) {
                TablePane(component.tableRouterState)
            }

            if (isMultiPane) {
                Box(modifier = Modifier.weight(DETAILS_PANE_WEIGHT))
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (isMultiPane) {
                Box(modifier = Modifier.weight(LIST_PANE_WEIGHT))
            }

            Box(modifier = Modifier.weight(if (isMultiPane) DETAILS_PANE_WEIGHT else 1F)) {
                DetailsPane(component.detailsRouterState)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun TablePane(routerState: Value<RouterState<*, Overview.TableChild>>) {
    Children(
        routerState = routerState,
    ) {
        when (val child = it.instance) {
            is Overview.TableChild.Table -> TableUi(child.component)
            is Overview.TableChild.None -> Box {}
        }.let {}
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun DetailsPane(routerState: Value<RouterState<*, Overview.DetailsChild>>) {
    Children(
        routerState = routerState,
        animation = childAnimation(fade() + scale())
    ) {
        when (val child = it.instance) {
            is Overview.DetailsChild.None -> Box {}
            is Overview.DetailsChild.Details -> DetailsUi(child.component)
            is Overview.DetailsChild.Updater -> BuilderUi(child.component)
        }.let {}
    }
}
