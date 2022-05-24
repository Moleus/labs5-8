package ru.moleus.kollector.feature.overview.table.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.observable.Observable
import common.context.EntityProvider
import model.data.Flat
import ru.moleus.kollector.data.local.model.table.FlatModel
import ru.moleus.kollector.feature.builder.util.toTableModel
import ru.moleus.kollector.feature.overview.table.EntitiesTable
import ru.moleus.kollector.feature.overview.table.EntitiesTable.*
import ru.moleus.kollector.utils.disposableScope

class EntitiesTableComponent(
    componentContext: ComponentContext,
    entityProvider: EntityProvider,
    selectedEntityId: Observable<Long?>,
    private val onEntitySelected: (id: Long) -> Unit
) : EntitiesTable, ComponentContext by componentContext, DisposableScope by componentContext.disposableScope() {

    private val _model = MutableValue(
        Model(
            entityModels = entityProvider.getAll().map(::toTableModel),
            defaultModel = FlatModel(Flat()),
            selectedEntityId = null
        )
    )

    override val model: Value<Model> = _model

    init {
        selectedEntityId.subscribeScoped { id ->
            _model.reduce { it.copy(selectedEntityId = id) }
        }
        entityProvider.subscribe { collection ->
            _model.reduce { it.copy(entityModels = collection.map(::toTableModel)) }
        }
    }

    override fun onEntityClicked(id: Long) {
        onEntitySelected(id)
    }
}