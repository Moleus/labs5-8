package ru.moleus.kollector.feature.builder.integration

import com.arkivanov.decompose.ComponentContext
import ru.moleus.kollector.feature.builder.store.EditorStore
import common.`entities-overview`.overview.util.toTableModel
import data.DtoBuilder
import data.Flat

class BuilderComponent(
    componentContext: ComponentContext,
    dtoBuilder: DtoBuilder,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
) : AbstractEditorComponent(
    componentContext,
    dtoBuilder,
    onClose,
    onSubmit,
) {
    override fun initValues(): Map<String, String> =
        toTableModel(Flat()).displayedAttributesInTable.filter { !it.label.equals("id", true) }.associate { Pair(it.label, "") }

    override fun onValueEntered(label: String, value: String) {
        store.accept(EditorStore.Intent.SetValue(label, value))
    }
}
