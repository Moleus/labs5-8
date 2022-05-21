package ru.moleus.kollector.ui.compose.overview.table.lazy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import bigLazyTable.view.table.LazyTable
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import overview.ui.table.lazy.header.HeaderRow
import ru.moleus.kollector.feature.overview.table.EntitiesTable

/**
 * @author Marco Sprenger, Livio Näf
 */
@Composable
fun RowScope.TableContainer(
    weight: Float,
    component: EntitiesTable,
) {
    val horizontalScrollState = rememberScrollState()
    val tableStore by component.store.subscribeAsState()

    Box(modifier = Modifier.weight(weight)) {
        Column(
            modifier = Modifier.padding(horizontal = HorizontalPadding),
            verticalArrangement = Arrangement.Top
        ) {
            HeaderRow(
                horizontalScrollState = horizontalScrollState,
                store = tableStore
            )
            LazyTable(
                tableStore = tableStore,
                horizontalScrollState = horizontalScrollState,
                onRowClick = { id -> component.onEntityClicked(id) }
            )
        }

//        HorizontalScrollbar(
//            adapter = rememberScrollbarAdapter(horizontalScrollState),
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .padding(end= 25.dp),
//        )
    }
}