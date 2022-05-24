package ru.moleus.kollector.ui.compose.overview.table

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.moleus.kollector.feature.overview.table.EntitiesTable
import ru.moleus.kollector.ui.compose.common.ScrollableList

/**
 * @author Marco Sprenger, Livio Näf
 */
@Composable
fun TableBody(
    model: EntitiesTable.Model,
    horizontalScrollState: ScrollState,
    onRowClick: (entityId: Long) -> Unit
) {
    //TODO: add isLoading to tableComponent
    if (model.isLoading) {
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    ScrollableList(modifier = Modifier.padding(bottom = ScrollbarMinimumHeight)) {
        items(items = model.entityModels) { entity ->
            TableRow(
                store = model,
                tableModel = entity,
                horizontalScrollState = horizontalScrollState,
                onClick = { onRowClick(entity.id.value) },
            )
        }
    }
}