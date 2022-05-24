package ru.moleus.kollector.ui.compose.overview.table.header

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.moleus.kollector.feature.overview.table.EntitiesTable
import ru.moleus.kollector.ui.compose.overview.table.HeaderCell
import ru.moleus.kollector.ui.compose.overview.table.HorizontalPadding

@Composable
fun HeaderRow(
    horizontalScrollState: ScrollState,
    model: EntitiesTable.Model
) {
    Row(
        modifier = Modifier
            .background(Color(0x0E325E))
            .fillMaxWidth()
            .padding(horizontal = HorizontalPadding)
            .horizontalScroll(horizontalScrollState),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (attribute in model.defaultModel.displayedAttributesInTable) {
            Column {
                Box(contentAlignment = Alignment.Center) {
                    HeaderCell(attribute = attribute)
                }
            }
        }
    }
}