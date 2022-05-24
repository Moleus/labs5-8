package ru.moleus.kollector.ui.compose.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
actual fun ScrollableList(modifier: Modifier, content: LazyListScope.() -> Unit) {
    val verticalLazyListState = rememberLazyListState()

    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = verticalLazyListState
        ) {
            content()
        }
    }
}

@Composable
actual fun BoxScope.MultiplatformHorizontalScrollbar(horizontalScrollState: ScrollState) {
//    adapter = rememberScrollbarAdapter(horizontalScrollState),
//    modifier = Modifier
//        .fillMaxWidth()
//        .align(Alignment.BottomCenter)
//        .padding(end = 25.dp),
}