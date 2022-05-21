package ru.moleus.kollector.feature.builder

import com.arkivanov.decompose.value.Value

interface Builder {
    val model: Value<Model>

    fun onValueEntered(label: String, value: String)

    fun onCloseClicked()

    fun onSubmitClicked()

    data class Model(
        val filledValues: Set<FieldInfo>,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMsg: String = "",
        val isSuccess: Boolean = false
    )
}