package ru.moleus.kollector.feature.builder

import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.observable.Observable

interface Builder {
    val model: Value<Model>
    val events: Observable<Event>

    fun onValueEntered(label: String, value: String)

    fun onCloseClicked()

    fun onSubmitClicked()

    data class Model(
        val filledValues: List<FieldInfo>,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMsg: String = "",
        val isSuccess: Boolean = false,
        val isToolbarVisible: Boolean,
    )

    sealed interface Event {
        data class MessageReceived(val message: String) : Event
    }
}