package ru.moleus.kollector.ui.compose.builder

import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.moleus.kollector.ui.compose.theme.LightCustomColors


@Composable
fun FieldTextInput(value: String, label: String, modifier: Modifier = Modifier, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(it) },
        label = { Text(label) },
        singleLine = true,
        placeholder = { Text(inputPrompt(label)) },
        modifier = modifier,
    )
}

private fun inputPrompt(text: String) = "Enter $text"

@Composable
fun ErrorText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        color = LightCustomColors.error
    )
}

@Composable
fun AddButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SubmitButton("Add", modifier, onClick)
}

@Composable
fun UpdateButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SubmitButton("Update", modifier, onClick)
}

@Composable
fun SubmitButton(text: String = "Submit", modifier: Modifier = Modifier, onSubmit: () -> Unit) {
    Button(
        onClick = onSubmit,
        modifier = modifier
    ) {
        Text(text)
    }
}

