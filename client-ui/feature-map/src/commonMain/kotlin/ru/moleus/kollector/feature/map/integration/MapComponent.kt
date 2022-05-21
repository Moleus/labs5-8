package ru.moleus.kollector.feature.map.integration

import com.arkivanov.decompose.ComponentContext
import common.context.EntityProvider
import ru.moleus.kollector.feature.map.EntitiesMap

class MapComponent(
    private val componentContext: ComponentContext,
    private val entityProvider: EntityProvider
): EntitiesMap {
}