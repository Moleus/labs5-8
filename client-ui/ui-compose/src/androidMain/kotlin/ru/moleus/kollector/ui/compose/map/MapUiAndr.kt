package ru.moleus.kollector.ui.compose.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import ru.moleus.kollector.ui.compose.R

@Composable
actual fun multiplatformLoadSvgPainter(imgPath: String): Painter {
    return androidx.compose.ui.res.painterResource(R.drawable.ic_house_with_garden)
}
