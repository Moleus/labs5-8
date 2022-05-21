package registration.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.moleus.kollector.feature.auth.Authentication
import ru.moleus.kollector.ui.compose.auth.FormUi
import ru.moleus.kollector.ui.compose.theme.MyTheme

@Composable
fun RegistrationUi(authentication: Authentication, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        FormUi(
            authentication,
            modifier = Modifier.width(350.dp).padding(30.dp),
        ) { shown ->
            LoggedInNotification(shown, Modifier.align(Alignment.TopEnd))
        }
    }
}

@Composable
fun LoggedInNotification(shown: Boolean, modifier: Modifier = Modifier) {
    val text = "Successfully logged in"
    Notification(
        isVisible = shown,
        modifier.height(45.dp).width(300.dp),
        text = text,
        color = MyTheme.customColors.success
    )
}