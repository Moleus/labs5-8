package ru.moleus.kollector.ui.compose.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
actual fun multiplatformLoadSvgPainter(imgPath: String): Painter {
    return painterResource(imgPath)
}