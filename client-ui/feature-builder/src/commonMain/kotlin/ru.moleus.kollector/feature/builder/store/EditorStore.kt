package ru.moleus.kollector.feature.builder.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.moleus.kollector.feature.builder.store.EditorStore.*

interface EditorStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data class SetValue(val label: String, val value: String) : Intent
        object Submit : Intent
    }

    data class State(
        val filledValues: Map<String, String>,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMsg: String = "",
        val isSuccess: Boolean = false
    )

    sealed interface Label
}