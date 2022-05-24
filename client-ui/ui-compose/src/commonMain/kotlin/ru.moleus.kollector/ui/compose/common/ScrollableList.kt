package ru.moleus.kollector.ui.compose.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ScrollableList(modifier: Modifier = Modifier, content: LazyListScope.() -> Unit)

@Composable
expect fun BoxScope.MultiplatformHorizontalScrollbar(horizontalScrollState: ScrollState)