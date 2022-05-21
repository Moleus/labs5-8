package ru.moleus.kollector.ui.compose.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.badoo.reaktive.disposable.scope.DisposableScope
import kotlinx.coroutines.launch
import registration.ui.DotsPulsing
import ru.moleus.kollector.feature.auth.Authentication

@Composable
fun FormUi(
    component: Authentication,
    modifier: Modifier = Modifier,
    onSuccess: @Composable (shown: Boolean) -> Unit
) {
    Scaffold(
//        topBar = {
//            AuthBar(
//                topInset = topInset
//            )
//        },
        content = {
            AuthBody(
                authentication = component,
                modifier = modifier
            )
        },
        snackbarHost = { hostState ->
            AuthMessage(
                hostState = hostState,
                authentication = component,
                bottomInset = 5.dp
            )
        }
    )
}

@Composable
fun AuthBody(authentication: Authentication, modifier: Modifier) {
    val model: State<Authentication.Model> = authentication.model.subscribeAsState()
    val state: Authentication.Model = model.value
    var buttonsModifier = Modifier.fillMaxWidth()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LoginField(state.login, Modifier.fillMaxWidth(), authentication::onLoginChanged)
        Spacer(Modifier.height(5.dp))
        PasswordField(state.password, Modifier.fillMaxWidth(), authentication::onPasswordChanged)
        Box {
            if (state.isLoading) {
                DotsPulsing(Modifier.align(Alignment.Center))
                buttonsModifier = buttonsModifier.blur(2.dp).alpha(0.8f)
            }
            SubmitButtons(
                buttonsModifier,
                authentication::onSubmitLoginClicked,
                authentication::onSubmitRegisterClicked
            )
        }
        if (state.isError) {
            println("Is error: ${state.errorMsg}")
            Text(text = state.errorMsg, color = Color.Red)
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
private fun AuthMessage(
    hostState: SnackbarHostState,
    authentication: Authentication,
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
    DisposableScope(authentication) {
        authentication.events.subscribeScoped { event ->
            when (event) {
                is Authentication.Event.MessageReceived -> {
                    scope.launch {
                        hostState.showSnackbar(event.message ?: "")
                    }
                }
            }
        }
    }
}

@Composable
fun LoginField(login: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = login,
        onValueChange = { onChange(it) },
        label = { Text("Login") },
        singleLine = true,
        placeholder = { Text("Login") },
        modifier = modifier,
    )
}

@Composable
fun PasswordField(password: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = { onChange(it) },
        label = { Text("Password") },
        singleLine = true,
        placeholder = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier,
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // TODO: provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
fun SubmitButtons(modifier: Modifier = Modifier, onLogin: () -> Unit, onRegister: () -> Unit) {
    Row(modifier = modifier) {
        Button(
            onClick = onLogin,
            modifier = Modifier.weight(5F)
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.weight(1F))
        Button(
            onClick = onRegister,
            modifier = Modifier.weight(5F)
        ) {
            Text("Register")
        }
    }
}