package ru.moleus.kollector.ui.compose.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.badoo.reaktive.disposable.scope.DisposableScope
import kotlinx.coroutines.launch
import ru.moleus.kollector.feature.auth.Authentication
import ru.moleus.kollector.ui.compose.common.MessageSnackbarHost

@Composable
fun RegistrationUi(authentication: Authentication, modifier: Modifier) {
    Scaffold(
        modifier = modifier,
        content = {
            Box(modifier, contentAlignment = Alignment.Center) {
                FormUi(
                    authentication,
                    modifier = Modifier.width(350.dp).padding(30.dp)
                )
            }
        },
        snackbarHost = { hostState ->
            AuthMessage(
                hostState = hostState,
                authentication = authentication,
                bottomInset = 5.dp
            )
        }
    )
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
    MessageSnackbarHost(hostState, bottomInset)

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
