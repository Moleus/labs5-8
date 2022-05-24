package ru.moleus.kollector.feature.builder.util

import ru.moleus.kollector.feature.builder.Builder
import ru.moleus.kollector.feature.builder.FieldInfo
import ru.moleus.kollector.feature.builder.store.EditorStore


internal val labelToEvent: (EditorStore.Label) -> Builder.Event =
    {
        when (it) {
            is EditorStore.Label.MessageReceived -> Builder.Event.MessageReceived(message = it.message)
        }
    }

fun Map<String, String>.toFieldInfo(): List<FieldInfo> =
    this.map { FieldInfo(label = it.key, value = it.value, errorMsg = "") }

fun <T : Collection<FieldInfo>> T.toFieldPairs(): Map<String, String> =
    this.associate { it.label to it.value }

fun <T : Collection<FieldInfo>> T.withoutErrors(): List<FieldInfo> =
    this.map { it.copy(errorMsg = "") }

fun <T : Collection<FieldInfo>> T.updateField(newField: FieldInfo): List<FieldInfo> =
    this.map { if (it.label == newField.label) newField else it }