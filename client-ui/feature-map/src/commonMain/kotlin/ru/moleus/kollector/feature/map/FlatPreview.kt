package ru.moleus.kollector.feature.map

data class FlatPreview(
    val userId: Long,
    val entityId: Long,
    val coordinates: Pair<Float, Float>,
    val colorCode: Long
)
