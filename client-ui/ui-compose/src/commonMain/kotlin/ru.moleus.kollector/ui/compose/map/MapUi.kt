package ru.moleus.kollector.ui.compose.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.moleus.kollector.feature.map.EntitiesMap
import ru.moleus.kollector.feature.map.FlatPreview
import kotlin.math.absoluteValue


const val HOUSE_IMG_PATH = "house-with-garden.svg"
const val PADDING = 100f

@Composable
fun MapUi(component: EntitiesMap, modifier: Modifier) {
    val model by component.model.subscribeAsState()
    val entitiesPreview = model.entitiesPreview
    MapContainer(entitiesPreview, modifier)
}

@Composable
fun MapContainer(
    entitiesPreview: List<FlatPreview>,
    modifier: Modifier
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val screenWidth = this@BoxWithConstraints.maxWidth.value
        val screenHeight = this@BoxWithConstraints.maxHeight.value
        val wSize = Offset(screenWidth, screenHeight)

        val maxValueX: Float = entitiesPreview.maxOf { it.coordinates.first.absoluteValue }
        val maxValueY: Float = entitiesPreview.maxOf { it.coordinates.second.absoluteValue }
        val scaleFactor = Offset(
            (screenWidth - PADDING) / (2 * maxValueX), ((screenHeight - PADDING) / (2 * maxValueY))
        )

        val houseImg: Painter = multiplatformLoadSvgPainter(HOUSE_IMG_PATH)

        Map(wSize, scaleFactor, houseImg, entitiesPreview)
    }
}

@Composable
expect fun multiplatformLoadSvgPainter(imgPath: String): Painter

@Composable
private fun Map(wSize: Offset, scaleFactor: Offset, entityImage: Painter, entitiesPreview: List<FlatPreview>) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        Axis(wSize)

        for (item in entitiesPreview) {
            val cords = item.coordinates.toPoint()
            val color = Color(item.colorCode).copy(0.8f)
            val drawing = Drawing(image = entityImage, color = color, Size(50f, 50f))
            ImageWithMarkers(drawing, cords, wSize, scaleFactor)
        }
    }
}