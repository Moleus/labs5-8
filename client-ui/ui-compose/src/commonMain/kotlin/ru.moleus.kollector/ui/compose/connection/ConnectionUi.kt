package ru.moleus.kollector.ui.compose.connection

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.badoo.reaktive.disposable.scope.DisposableScope
import kotlinx.coroutines.launch
import registration.ui.DotsPulsing
import ru.moleus.kollector.feature.connection.Connection

@Composable
fun ConnectionUi(
    component: Connection,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        content = {
            Box(modifier, contentAlignment = Alignment.Center) {
                ConnectionBody(
                    component = component,
                    modifier = Modifier.width(350.dp).padding(30.dp)
                )
            }
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
fun ConnectionBody(component: Connection, modifier: Modifier) {
    val model by component.model.subscribeAsState()
    var buttonsModifier = Modifier.fillMaxWidth()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        InputField(model.serverIp, "server ip", "input server ip", Modifier.fillMaxWidth(), component::onIpChanged)
        Spacer(Modifier.height(5.dp))
        InputField(
            model.serverPort,
            "server port",
            "input server port",
            Modifier.fillMaxWidth(),
            component::onPortChanged
        )
        Box {
            if (model.isLoading) {
                DotsPulsing(Modifier.align(Alignment.Center))
                buttonsModifier = buttonsModifier.blur(2.dp).alpha(0.8f)
            }
            Button(
                onClick = component::onConnectClicked,
            ) {
                Text("Connect")
            }
        }
    }
}

@Composable
fun DisposableScope(key: Any, block: DisposableScope.() -> Unit) {
    DisposableEffect(key) {
        val scope = DisposableScope()
        scope.block()
        onDispose(scope::dispose)
    }
}

@Composable
private fun Message(
    hostState: SnackbarHostState,
    component: Connection,
    bottomInset: Dp
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = {
            Snackbar(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(hostState.currentSnackbarData?.message ?: "")
            }
        },
        modifier = Modifier.padding(bottom = bottomInset)
    )

    val scope = rememberCoroutineScope()
    DisposableScope(component) {
        component.events.subscribeScoped { event ->
            when (event) {
                is Connection.Event.MessageReceived -> {
                    scope.launch {
                        hostState.showSnackbar(event.message ?: "")
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(
    text: String,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onChange(it) },
        label = { Text(label) },
        singleLine = true,
        placeholder = { Text(placeholder) },
        modifier = modifier,
    )
}
