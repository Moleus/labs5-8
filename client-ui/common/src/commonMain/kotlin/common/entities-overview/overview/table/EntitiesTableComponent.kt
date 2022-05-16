package overview.table

import data.EntityProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.observable.Observable
import common.`entities-overview`.overview.table.store.TableStore
import data.Flat
import common.`entities-overview`.overview.util.toTableModel
import overview.ui.table.lazy.model.FlatModel
import overview.util.disposableScope

class EntitiesTableComponent(
    componentContext: ComponentContext,
    entityProvider: EntityProvider,
    selectedEntityId: Observable<Long?>,
    private val onEntitySelected: (id: Long) -> Unit
) : EntitiesTable, ComponentContext by componentContext, DisposableScope by componentContext.disposableScope() {

    private val _model = MutableValue(
        TableStore(
            entityModels = entityProvider.getAll().map(::toTableModel),
            defaultModel = FlatModel(Flat()),
            selectedEntityId = null
        )
    )

    override val store: Value<TableStore> = _model

    init {
        selectedEntityId.subscribeScoped { id ->
            _model.reduce { it.copy(selectedEntityId = id) }
        }
    }

    override fun onEntityClicked(id: Long) {
        onEntitySelected(id)
    }
}