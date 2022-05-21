package ru.moleus.kollector.ui.compose.builder

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ru.moleus.kollector.feature.builder.Builder

@Composable
fun BuilderUi(builder: Builder, modifier: Modifier = Modifier) {
    val model = builder.model.subscribeAsState()
    val fields = model.value.filledValues

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        for (field in fields) {
            FieldTextInput(value = field.value, label = field.label, onChange = {builder.onValueEntered(field.label, it) })
            if (field.errorMsg.isNotEmpty()) {
                ErrorText(text = field.errorMsg)
            }
            //show error near field with error message
        }
        SubmitButton { builder.onSubmitClicked() }
    }
}
