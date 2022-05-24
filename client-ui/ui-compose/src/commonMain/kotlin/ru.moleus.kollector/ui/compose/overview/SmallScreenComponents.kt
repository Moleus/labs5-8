package ru.moleus.kollector.ui.compose.overview

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopBarWithSubmit(
    modifier: Modifier = Modifier,
    text: String,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = text) },
        modifier = modifier,
        actions = actions
    )
}

@Composable
fun TopBarWithBack(
    modifier: Modifier = Modifier,
    text: String,
    actions: @Composable RowScope.() -> Unit,
    onBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = text) },
        modifier = modifier,
        actions = actions,
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}
