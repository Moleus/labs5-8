package ru.moleus.kollector.ui.compose.map

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

actual fun DrawScope.drawText(text: String, size: Float, point: Offset) {
    drawIntoCanvas {
        it.nativeCanvas.drawText(
            text,
            point.x,
            point.y,
            Paint()
        )
    }
}
