package ru.moleus.kollector.ui.compose.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import registration.ui.DotsPulsing
import ru.moleus.kollector.feature.auth.Authentication

@Composable
fun FormUi(
    authentication: Authentication,
    modifier: Modifier = Modifier,
) {
    val model by authentication.model.subscribeAsState()
    var buttonsModifier = Modifier.fillMaxWidth()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginField(model.login, Modifier.fillMaxWidth(), authentication::onLoginChanged)
        Spacer(Modifier.height(5.dp))
        PasswordField(model.password, Modifier.fillMaxWidth(), authentication::onPasswordChanged)
        Box {
            if (model.isLoading) {
                DotsPulsing(Modifier.align(Alignment.Center))
                buttonsModifier = buttonsModifier.blur(2.dp).alpha(0.8f)
            }
            SubmitButtons(
                buttonsModifier,
                authentication::onSubmitLoginClicked,
                authentication::onSubmitRegisterClicked
            )
        }
    }
}

@Composable
fun SubmitButtons(modifier: Modifier = Modifier, onLogin: () -> Unit, onRegister: () -> Unit) {
    Row(modifier = modifier) {
        Button(
            onClick = onLogin,
            modifier = Modifier.weight(5F)
        ) {
            Text("Login", overflow = TextOverflow.Ellipsis)
        }
        Spacer(modifier = Modifier.weight(1F))
        Button(
            onClick = onRegister,
            modifier = Modifier.weight(5F)
        ) {
            Text("Register", overflow = TextOverflow.Ellipsis)
        }
    }
}