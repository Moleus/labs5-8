package ru.moleus.kollector.feature.builder.integration

import com.arkivanov.decompose.ComponentContext
import commands.ExecutionPayload
import model.builder.ModelDtoBuilderWrapper
import model.data.Flat
import ru.moleus.kollector.domain.client.RemoteCommandExecutor
import common.context.Exchanger
import model.ModelDto
import model.builder.BuilderWrapper
import ru.moleus.kollector.feature.builder.store.EditorStore
import ru.moleus.kollector.feature.builder.util.toTableModel


class UpdaterComponent(
    componentContext: ComponentContext,
    dtoBuilder: BuilderWrapper<ModelDto>,
    exchanger: Exchanger,
    entityId: Long,
    onClose: () -> Unit,
) : AbstractBuilderComponent(
    componentContext = componentContext,
    commandExecutor = RemoteCommandExecutor(exchanger),
    dtoBuilder = dtoBuilder,
    executionPayload = ExecutionPayload.of("update", entityId.toString()),
    onClose = onClose,
) {
    override fun initValues(): Map<String, String> =
        toTableModel(Flat()).displayedAttributesInTable.filter { !it.label.equals("id", true) }.associate { Pair(it.label, "") }

    override fun onValueEntered(label: String, value: String) {
        store.accept(EditorStore.Intent.SetValue(label, value))
    }
}