package ru.moleus.kollector.feature.auth

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable

/**
 * Public methods are called from Ui layer.
 */
interface Authentication {
    val model: Value<Model>
    val events: Observable<Event>

    fun onLoginChanged(login: String)

    fun onPasswordChanged(password: String)

    fun onSubmitLoginClicked()

    fun onSubmitRegisterClicked()

    sealed interface Event {
        data class MessageReceived(val message: String?): Event
    }

    data class Model(
        val login: String,
        val password: String,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMsg: String = "",
        val isLoggedIn: Boolean = false
    )
}