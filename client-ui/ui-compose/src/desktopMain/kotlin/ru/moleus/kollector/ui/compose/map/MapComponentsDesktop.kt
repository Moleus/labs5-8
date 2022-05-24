package ru.moleus.kollector.ui.compose.map

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Font

actual fun DrawScope.drawText(text: String, size: Float, point: Offset) {
    drawIntoCanvas {
        it.nativeCanvas.drawString(
            text,
            point.x,
            point.y,
            font = Font(typeface = null, size = 10f),
            paint = org.jetbrains.skia.Paint()
        )
    }
}
