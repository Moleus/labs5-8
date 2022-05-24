package ru.moleus.kollector.ui.compose.map

import androidx.compose.ui.geometry.Offset


fun Offset.xAxis(): Float = this.x / 2
fun Offset.yAxis(): Float = this.y / 2

fun Point.scale(dx: Float, dy: Float) = Point(this.x * dx, this.y * dy)
fun Point.xVec() = this.scale(1f, 0f)
fun Point.yVec() = this.scale(0f, 1f)

fun Point.toAbs(wSize: Offset): Offset = Offset(this.x + wSize.xAxis(), wSize.yAxis() - this.y)


fun Pair<Float, Float>.toPoint() = Point(x = this.first, y = this.second)


/**
 * lower bound is 15. (when x is 0)
 * upper bound is 40. (when x is > 10)
 */
fun sigmoid(x: Float) = 35f / (1 + kotlin.math.exp(1 - x)) + 5