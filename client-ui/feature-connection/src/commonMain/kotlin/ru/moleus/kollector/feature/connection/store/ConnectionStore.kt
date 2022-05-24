package ru.moleus.kollector.feature.connection.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.moleus.kollector.feature.connection.store.ConnectionStore.*

interface ConnectionStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data class SetIp(val ip: String) : Intent
        data class SetPort(val port: String) : Intent
        object SubmitConnect : Intent
    }

    data class State(
        val serverIp: String,
        val serverPort: String,
        val isLoading: Boolean = false,
    )

    sealed interface Label {
        data class MessageReceived(val message: String?) : Label
    }
}