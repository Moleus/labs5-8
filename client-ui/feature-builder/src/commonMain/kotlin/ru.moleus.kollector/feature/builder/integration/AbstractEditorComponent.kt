package ru.moleus.kollector.feature.builder.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.moleus.kollector.feature.builder.store.EditorStore
import ru.moleus.kollector.feature.builder.store.EditorStoreProvider
import common.shared.util.asValue
import data.DtoBuilder
import editor.Editor

abstract class AbstractEditorComponent(
    componentContext: ComponentContext,
    dtoBuilder: DtoBuilder,
    private val onClose: () -> Unit,
    private val onSubmit: () -> Unit,
) : Editor {

    protected abstract fun initValues(): Map<String, String>

    protected val store =
        componentContext.instanceKeeper.getStore {
            EditorStoreProvider(
                storeFactory = DefaultStoreFactory(),
                dtoBuilder = dtoBuilder,
                initialValues = initValues()
            ).provide()
        }

    override val model = store.asValue().map(stateToModel)


    override fun onValueEntered(label: String, value: String) {
        store.accept(EditorStore.Intent.SetValue(label, value))
    }

    override fun onCloseClicked() {
        onClose()
    }

    override fun onSubmitClicked() {
        onSubmit()
    }
}
