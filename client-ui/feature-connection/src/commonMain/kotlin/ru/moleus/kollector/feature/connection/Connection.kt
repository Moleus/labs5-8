package ru.moleus.kollector.feature.connection

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable

interface Connection {
    val model: Value<Model>
    val events: Observable<Event>

    fun onIpChanged(ip: String)
    fun onPortChanged(port: String)
    fun onConnectClicked()

    data class Model(
        val serverIp: String,
        val serverPort: String,
        val isLoading: Boolean
    )

    sealed interface Event {
        data class MessageReceived(val message: String?) : Event
    }
}