package ru.moleus.kollector.ui.compose.overview.table

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.moleus.kollector.feature.overview.table.EntitiesTable


@Composable
fun TableUi(component: EntitiesTable, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxSize()) {
        TableContainer(
            weight = 2f,
            component = component,
        )
    }
}