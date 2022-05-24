package ru.moleus.kollector.feature.map.integration

import model.data.Flat
import ru.moleus.kollector.feature.map.EntitiesMap
import ru.moleus.kollector.feature.map.FlatPreview
import ru.moleus.kollector.feature.map.store.MapStore

internal val stateToModel: (MapStore.State) -> (EntitiesMap.Model) =
    {
        EntitiesMap.Model(
            entitiesPreview = it.entities.toPreviews()
        )
    }


private fun Set<Flat>.toPreviews() =
    this.map {
        FlatPreview(
            userId = it.userId,
            entityId = it.id,
            coordinates = Pair(it.coordinates.x.toFloat(), it.coordinates.y.toFloat()),
            colorCode = it.userId.toColorCode()
        )
    }


/**
 * takes any number or digit and returns "hashed" 6-digit number
 */
private fun Long.toColorCode() =
    ("FF" + ((((this + 100) shl 24).toString()).hashCode()).toString().takeLast(6)).toLong(16)
