package bigLazyTable.view.table

import androidx.compose.foundation.ScrollState
//import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.moleus.kollector.feature.overview.table.store.TableStore
import ru.moleus.kollector.ui.compose.overview.table.lazy.TableRow

//import common.`entities-overview`.overview.ui.table.lazy.CustomScrollbarStyle
//import common.`entities-overview`.ru.moleus.kollector.ui.compose.overview.table.lazy.getScrollbarMinimumHeight
//import common.`entities-overview`.ru.moleus.kollector.ui.compose.overview.table.lazy.getScrollbarThickness

/**
 * @author Marco Sprenger, Livio Näf
 */
@Composable
fun LazyTable(
    tableStore : TableStore,
    horizontalScrollState: ScrollState,
    onRowClick : (entityId: Long) -> Unit
) {
    val verticalLazyListState = rememberLazyListState()

    //TODO: add isLoading to tableComponent
    if (tableStore.isLoading) {
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .padding(bottom = ScrollbarMinimumHeight)
    ) {
        // Go to the top when start/stop filtering
        LazyColumn(
//            modifier = Modifier.padding(end = ScrollbarThickness),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            state = verticalLazyListState
        ) {
            val lazyListItems = tableStore.entityModels
            items(items = lazyListItems) { entity ->
                TableRow(
                    store = tableStore,
                    tableModel = entity,
                    horizontalScrollState = horizontalScrollState,
                    onClick = { onRowClick(entity.id.value) }
                )
            }
        }

//        VerticalScrollbar(
//            adapter = rememberScrollbarAdapter(verticalLazyListState),
//            modifier = Modifier
//                .align(Alignment.CenterEnd)
//                .fillMaxHeight(),
//            style = CustomScrollbarStyle
//        )
    }
}