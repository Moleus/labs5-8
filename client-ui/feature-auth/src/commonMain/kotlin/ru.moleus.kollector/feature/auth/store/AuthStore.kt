package ru.moleus.kollector.feature.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.moleus.kollector.feature.auth.store.AuthStore.*

internal interface AuthStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data class SetLogin(val login: String) : Intent
        data class SetPassword(val password: String) : Intent
        object SubmitLogin : Intent
        object SubmitRegister : Intent
    }

    data class State(
        val login: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMsg: String = "",
    )

    sealed interface Label {
        data class MessageReceived(val message: String?) : Label
    }
}