package ru.moleus.kollector.feature.map

import com.arkivanov.decompose.value.Value

interface EntitiesMap {
    val model: Value<Model>

    data class Model(
        val entitiesPreview: List<FlatPreview>,
    )
}