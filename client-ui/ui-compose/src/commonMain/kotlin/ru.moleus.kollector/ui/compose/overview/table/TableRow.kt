package ru.moleus.kollector.ui.compose.overview.table

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.moleus.kollector.data.local.model.table.TableModel
import ru.moleus.kollector.feature.overview.table.EntitiesTable

/**
 * @author Marco Sprenger, Livio Näf
 */
@Composable
fun TableRow(
    store: EntitiesTable.Model,
    tableModel: TableModel,
    horizontalScrollState: ScrollState,
    onClick: () -> Unit
) {
    val isSelected = store.selectedEntityId == tableModel.id.value
    val backgroundColor = if (isSelected) Color.LightGray else Color.White

    Row(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(horizontal = HorizontalPadding)
            .selectable(
                selected = isSelected,
                onClick = onClick
            )
            .horizontalScroll(horizontalScrollState),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (attribute in tableModel.displayedAttributesInTable) {
            TableCell(attribute = attribute)
        }
    }
}