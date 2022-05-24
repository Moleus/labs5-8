package ru.moleus.kollector.feature.map.store

import com.arkivanov.mvikotlin.core.store.Store
import model.data.Flat
import ru.moleus.kollector.feature.map.store.MapStore.*

interface MapStore : Store<Intent, State, Any> {
    sealed interface Intent {
        data class Update(val collection: Set<Flat>) : Intent
    }

    data class State(
        val entities: Set<Flat> = setOf(),
    )
}