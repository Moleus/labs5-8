package overview.ui.table.lazy.header

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.moleus.kollector.ui.compose.overview.table.lazy.HeaderCell
import ru.moleus.kollector.feature.overview.table.store.TableStore
import ru.moleus.kollector.ui.compose.overview.table.lazy.HorizontalPadding

@Composable
fun HeaderRow(
    horizontalScrollState: ScrollState,
    store: TableStore
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
        for (attribute in store.defaultModel.displayedAttributesInTable) {
            Column {
                Box(contentAlignment = Alignment.Center) {
                    HeaderCell(attribute = attribute)
                }
            }
        }
    }
}