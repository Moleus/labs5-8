package ru.moleus.kollector.feature.builder.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.moleus.kollector.feature.builder.FieldInfo
import ru.moleus.kollector.feature.builder.store.EditorStore.*

interface EditorStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data class SetValue(val label: String, val value: String) : Intent
        data class SetToolbarVisible(val isToolbarVisible: Boolean) : Intent
        object Submit : Intent
    }

    data class State(
        val filledValues: List<FieldInfo>,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMsg: String = "",
        val isSuccess: Boolean = false,
        val isToolbarVisible: Boolean = false
    )

    sealed interface Label {
        data class MessageReceived(val message: String) : Label
    }
}