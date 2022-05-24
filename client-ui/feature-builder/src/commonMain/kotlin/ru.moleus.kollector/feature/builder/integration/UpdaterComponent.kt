package ru.moleus.kollector.feature.builder.integration

import com.arkivanov.decompose.ComponentContext
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.observable.Observable
import commands.ExecutionPayload
import common.context.Exchanger
import model.ModelDto
import model.builder.BuilderWrapper
import model.data.Flat
import ru.moleus.kollector.data.local.model.table.HeaderLabel
import ru.moleus.kollector.domain.client.RemoteCommandExecutor
import ru.moleus.kollector.feature.builder.store.EditorStore
import ru.moleus.kollector.feature.builder.util.toTableModel
import ru.moleus.kollector.utils.disposableScope


class UpdaterComponent(
    componentContext: ComponentContext,
    dtoBuilder: BuilderWrapper<ModelDto>,
    exchanger: Exchanger,
    entityId: Long,
    isToolbarVisible: Observable<Boolean>,
    onClose: () -> Unit,
) : AbstractBuilderComponent(
    componentContext = componentContext,
    commandExecutor = RemoteCommandExecutor(exchanger),
    dtoBuilder = dtoBuilder,
    executionPayload = ExecutionPayload.of("update", entityId.toString()),
    onClose = onClose,
), DisposableScope by componentContext.disposableScope() {
    init {
        isToolbarVisible.subscribeScoped {
            store.accept(EditorStore.Intent.SetToolbarVisible(it))
        }
    }

    override fun initValues(): Map<String, String> =
        toTableModel(Flat()).displayedAttributesInTable.filter {
            it.label !in listOf(
                HeaderLabel.ID,
                HeaderLabel.CREATION_DATE
            )
        }.associate { Pair(it.label.eng, "") }

    override fun onValueEntered(label: String, value: String) {
        store.accept(EditorStore.Intent.SetValue(label, value))
    }
}