package ru.moleus.kollector.feature.builder.util

import ru.moleus.kollector.feature.builder.FieldInfo


fun Map<String, String>.toFieldInfo(): Set<FieldInfo> =
    this.map { FieldInfo(label = it.key, value = it.value, errorMsg = "") }.toSet()

fun Set<FieldInfo>.toFieldPairs(): Map<String, String> =
    this.associate { it.label to it.value }

fun Set<FieldInfo>.withoutErrors(): Set<FieldInfo> =
    this.map { it.copy(errorMsg = "") }.toSet()