package ru.moleus.kollector.feature.builder.integration

import com.arkivanov.decompose.ComponentContext
import commands.ExecutionPayload
import common.context.CommandExecutor
import model.ModelDto
import model.builder.BuilderWrapper
import model.data.Flat
import ru.moleus.kollector.data.local.model.table.HeaderLabel.*
import ru.moleus.kollector.feature.builder.store.EditorStore
import ru.moleus.kollector.feature.builder.util.toTableModel

class BuilderComponent(
    componentContext: ComponentContext,
    dtoBuilder: BuilderWrapper<ModelDto>,
    commandExecutor: CommandExecutor,
    onClose: () -> Unit,
) : AbstractBuilderComponent(
    componentContext = componentContext,
    commandExecutor = commandExecutor,
    dtoBuilder = dtoBuilder,
    executionPayload = ExecutionPayload.of("add", null),
    onClose = onClose,
) {

    override fun initValues(): Map<String, String> =
        toTableModel(Flat()).displayedAttributesInTable.filter { it.label !in listOf(ID, CREATION_DATE) }
            .associate { Pair(it.label.eng, "") }

    override fun onValueEntered(label: String, value: String) {
        store.accept(EditorStore.Intent.SetValue(label, value))
    }
}
