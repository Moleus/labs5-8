package ru.moleus.kollector.feature.builder.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import commands.ExecutionPayload
import common.context.CommandExecutor
import model.ModelDto
import model.builder.BuilderWrapper
import ru.moleus.kollector.feature.builder.Builder
import ru.moleus.kollector.feature.builder.store.EditorStore
import ru.moleus.kollector.feature.builder.store.EditorStoreProvider
import ru.moleus.kollector.feature.builder.util.labelToEvent
import ru.moleus.kollector.utils.asValue

abstract class AbstractBuilderComponent(
    componentContext: ComponentContext,
    commandExecutor: CommandExecutor,
    dtoBuilder: BuilderWrapper<ModelDto>,
    private val executionPayload: ExecutionPayload,
    private val onClose: () -> Unit,
) : Builder {

    protected abstract fun initValues(): Map<String, String>

    protected val store =
        componentContext.instanceKeeper.getStore {
            EditorStoreProvider(
                storeFactory = DefaultStoreFactory(),
                dtoBuilder = dtoBuilder,
                commandExecutor = commandExecutor,
                executionPayload = executionPayload,
                initialValues = initValues(),
            ).provide()
        }

    override val model = store.asValue().map(stateToModel)
    override val events: Observable<Builder.Event> = store.labels.map(labelToEvent)

    override fun onValueEntered(label: String, value: String) {
        store.accept(EditorStore.Intent.SetValue(label, value))
    }

    override fun onCloseClicked() {
        onClose()
    }

    override fun onSubmitClicked() {
        store.accept(EditorStore.Intent.Submit)
    }
}
