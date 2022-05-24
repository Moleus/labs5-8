package ru.moleus.kollector.ui.compose.map

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter

data class Drawing(val image: Painter, val color: Color, val size: Size)

data class Point(val x: Float, val y: Float)

fun DrawScope.Axis(wSize: Offset) {
    val verticalLine = Pair(Offset(wSize.xAxis(), 0f), Offset(wSize.xAxis(), wSize.y))
    val horizontalLine = Pair(Offset(0f, wSize.yAxis()), Offset(wSize.x, wSize.yAxis()))

    drawLine(Color.Black, start = verticalLine.first, end = verticalLine.second)
    drawLine(Color.Black, start = horizontalLine.first, end = horizontalLine.second)
}

fun DrawScope.PointWithMarkers(color: Color, cords: Point, wSize: Offset, scale: Offset) {
    Point(color, cords.toAbs(wSize))
    MarkersOnAxis(cords, wSize, scale)
}

fun DrawScope.ImageWithMarkers(image: Drawing, cords: Point, wSize: Offset, scale: Offset) {
    Image(image, cords.scale(scale.x, scale.y).toAbs(wSize), scale)
    MarkersOnAxis(cords, wSize, scale)
}

fun DrawScope.Image(drawing: Drawing, position: Offset, scale: Offset) {
    val boundedX = sigmoid(scale.x)
    val size = Size(boundedX, boundedX)
    drawIntoCanvas { canvas ->
        canvas.withSave {
            with(drawing.image) {
                // move image center to point.
                translate(position.x - size.width / 2, position.y - size.height / 2) {
                    draw(size, colorFilter = ColorFilter.tint(drawing.color, blendMode = BlendMode.SrcAtop))
                }
            }
        }
    }
}

fun DrawScope.Point(color: Color, center: Offset) {
    drawCircle(color, radius = 10f, center = center)
}

fun DrawScope.MarkersOnAxis(point: Point, windowSize: Offset, scale: Offset) {
    XMarker(point.xVec().scale(scale.x, scale.y).toAbs(windowSize), 10f, String.format("%.1f", point.x))
    YMarker(point.yVec().scale(scale.x, scale.y).toAbs(windowSize), 10f, String.format("%.1f", point.y))
    dottedToAxis(point.scale(scale.x, scale.y).toAbs(windowSize), windowSize)
}


fun DrawScope.dottedToAxis(point: Offset, windowSize: Offset) {
    dashedLine(start = point, end = Offset(point.x, windowSize.yAxis()))
    dashedLine(start = point, end = Offset(windowSize.xAxis(), point.y))
}

fun DrawScope.dashedLine(start: Offset, end: Offset) =
    drawLine(
        color = Color.Black,
        start = start,
        end = end,
        pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 10f), phase = 20f)
    )

fun DrawScope.XMarker(point: Offset, width: Float, value: String?) {
    val half = width / 2
    drawLine(Color.Black, start = Offset(point.x, point.y - half), end = Offset(point.x, point.y + half))
    value?.let { LabelX(it, point, half + 2) }
}

fun DrawScope.YMarker(point: Offset, height: Float, value: String?) {
    val half = height / 2
    drawLine(Color.Black, start = Offset(point.x - half, point.y), end = Offset(point.x + half, point.y))
    value?.let { LabelY(it, point, half + 2) }
}

fun DrawScope.LabelX(value: String, point: Offset, yOffset: Float) {
    val fontSize = 10f
    val shift = Offset(-value.length.toFloat() * 2f, yOffset + fontSize)
    drawText(value, fontSize, point.plus(shift))
}

fun DrawScope.LabelY(value: String, point: Offset, xOffset: Float) {
    val fontSize = 10f
    val shift = Offset(xOffset, fontSize / 2 - 2)
    drawText(value, fontSize, point.plus(shift))
}

expect fun DrawScope.drawText(text: String, size: Float, point: Offset)