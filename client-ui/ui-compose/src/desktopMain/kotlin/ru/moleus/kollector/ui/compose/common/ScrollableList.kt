package ru.moleus.kollector.ui.compose.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.moleus.kollector.ui.compose.overview.table.HoverColor
import ru.moleus.kollector.ui.compose.overview.table.ScrollbarMinimumHeight
import ru.moleus.kollector.ui.compose.overview.table.ScrollbarThickness
import ru.moleus.kollector.ui.compose.overview.table.UnhoverColor

@Composable
actual fun ScrollableList(modifier: Modifier, content: LazyListScope.() -> Unit) {
    val verticalLazyListState = rememberLazyListState()

    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier.padding(end = ScrollbarThickness),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = verticalLazyListState
        ) {
            content()
        }
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(verticalLazyListState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            style = CustomScrollbarStyle
        )
    }
}

@Composable
actual fun BoxScope.MultiplatformHorizontalScrollbar(horizontalScrollState: ScrollState) {
    HorizontalScrollbar(
        adapter = rememberScrollbarAdapter(horizontalScrollState),
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(end = 25.dp),

        )
}

val CustomScrollbarStyle = ScrollbarStyle(
    minimalHeight = ScrollbarMinimumHeight,
    thickness = ScrollbarThickness,
    shape = RoundedCornerShape(4.dp),
    hoverDurationMillis = 1000,
    hoverColor = HoverColor,
    unhoverColor = UnhoverColor
)

