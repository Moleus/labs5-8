package ru.moleus.kollector.ui.compose.overview.table

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.moleus.kollector.feature.overview.table.EntitiesTable
import ru.moleus.kollector.ui.compose.common.MultiplatformHorizontalScrollbar
import ru.moleus.kollector.ui.compose.overview.table.header.HeaderRow

/**
 * @author Marco Sprenger, Livio Näf
 */
@Composable
fun RowScope.TableContainer(
    weight: Float,
    component: EntitiesTable,
) {
    val horizontalScrollState = rememberScrollState()
    val modelState by component.model.subscribeAsState()

    Box(modifier = Modifier.weight(weight)) {
        Column(
            modifier = Modifier.padding(horizontal = HorizontalPadding),
            verticalArrangement = Arrangement.Top
        ) {
            HeaderRow(
                horizontalScrollState = horizontalScrollState,
                model = modelState
            )
            TableBody(
                model = modelState,
                horizontalScrollState = horizontalScrollState,
                onRowClick = { id -> component.onEntityClicked(id) }
            )
        }

        MultiplatformHorizontalScrollbar(horizontalScrollState)
    }
}