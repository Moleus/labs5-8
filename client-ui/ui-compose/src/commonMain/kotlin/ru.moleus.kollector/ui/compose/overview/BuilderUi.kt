package editor.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import editor.Editor

@Composable
fun BuilderUi(builder: Editor, modifier: Modifier = Modifier) {
    val model = builder.model.subscribeAsState()
    val fields = model.value.filledValues

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        for ((label, value) in fields) {
            FieldTextInput(value = value, label = label, onChange = {builder.onValueEntered(label, it) })
        }
        AddButton { builder.onSubmitClicked() }
    }
}
