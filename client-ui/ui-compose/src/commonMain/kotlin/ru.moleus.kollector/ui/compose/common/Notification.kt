package ru.moleus.kollector.ui.compose.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MessageSnackbarHost(
    hostState: SnackbarHostState,
    bottomInset: Dp,
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = {
            Snackbar(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(hostState.currentSnackbarData?.message ?: "", color = MaterialTheme.colors.primaryVariant)
            }
        },
        modifier = Modifier.padding(bottom = bottomInset)
    )
}

