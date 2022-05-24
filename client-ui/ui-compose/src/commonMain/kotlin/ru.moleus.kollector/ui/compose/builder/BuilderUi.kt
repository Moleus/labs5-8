package ru.moleus.kollector.ui.compose.builder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.launch
import ru.moleus.kollector.feature.builder.Builder
import ru.moleus.kollector.ui.compose.auth.DisposableScope
import ru.moleus.kollector.ui.compose.common.MessageSnackbarHost
import ru.moleus.kollector.ui.compose.common.ScrollableList
import ru.moleus.kollector.ui.compose.overview.TopBarWithBack
import ru.moleus.kollector.ui.compose.overview.TopBarWithSubmit

@Composable
fun UpdaterUi(component: Builder, modifier: Modifier = Modifier) {
    BuilderScreen(component, modifier) {
        UpdaterTopBar(component)
    }
}

@Composable
fun CreatorUi(component: Builder, modifier: Modifier = Modifier) {
    BuilderScreen(component, modifier) {
        CreatorTopBar(component)
    }
}

@Composable
private fun BuilderScreen(component: Builder, modifier: Modifier, topBar: @Composable (Builder) -> Unit) {
    Scaffold(
        content = {
            Body(
                component = component,
                modifier = modifier,
                topBar = topBar
            )
        },
        snackbarHost = { hostState ->
            Message(
                hostState = hostState,
                component = component,
                bottomInset = 5.dp
            )
        }
    )
}

@Composable
private fun Body(component: Builder, modifier: Modifier = Modifier, topBar: @Composable (Builder) -> Unit) {
    val model by component.model.subscribeAsState()

    Column(modifier = modifier.fillMaxSize()) {
        topBar(component)
        ScrollableList(modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp)) {
            items(items = model.filledValues) { field ->
                FieldTextInput(value = field.value, label = field.label) {
                    component.onValueEntered(
                        field.label,
                        it
                    )
                }
                if (field.errorMsg.isNotEmpty()) {
                    ErrorText(text = field.errorMsg)
                }
            }
        }
    }
}

@Composable
private fun UpdaterTopBar(component: Builder) {
    TopBarWithBack(
        text = "Changing flat",
        onBack = component::onCloseClicked,
        actions = {
            SubmitButton { component.onSubmitClicked() }
        },
    )
}

@Composable
private fun CreatorTopBar(component: Builder) {
    TopBarWithSubmit(
        text = "Creating new flat",
        actions = {
            SubmitButton { component.onSubmitClicked() }
        },
    )
}

@Composable
private fun Message(
    hostState: SnackbarHostState,
    component: Builder,
    bottomInset: Dp
) {
    MessageSnackbarHost(hostState, bottomInset)

    val scope = rememberCoroutineScope()
    DisposableScope(component) {
        component.events.subscribeScoped { event ->
            when (event) {
                is Builder.Event.MessageReceived -> {
                    scope.launch {
                        hostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }
}
