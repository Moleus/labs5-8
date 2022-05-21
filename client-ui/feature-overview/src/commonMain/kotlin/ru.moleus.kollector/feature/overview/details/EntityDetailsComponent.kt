package ru.moleus.kollector.feature.overview.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.observable.Observable
import common.context.EntityProvider
import ru.moleus.kollector.feature.builder.util.toTableModel
import ru.moleus.kollector.feature.overview.details.EntityDetails.*
import ru.moleus.kollector.utils.disposableScope

class EntityDetailsComponent(
    componentContext: ComponentContext,
    entityProvider: EntityProvider,
    entityId: Long,
    isToolbarVisible: Observable<Boolean>,
    private val onComplete: () -> Unit,
    private val onUpdateButtonClicked: (entityId: Long) -> Unit,
) : EntityDetails, ComponentContext by componentContext, DisposableScope by componentContext.disposableScope() {

    private val _model =
        MutableValue(
            Model(
                isToolbarVisible = false,
                entityModel = toTableModel(entityProvider.getById(id = entityId))
            )
        )

    override val model: Value<Model> = _model

    init {
        isToolbarVisible.subscribeScoped { isVisible ->
            _model.reduce { it.copy(isToolbarVisible = isVisible) }
        }
    }

    override fun onUpdateClicked(entityId: Long) {
        onUpdateButtonClicked(entityId)
    }

    override fun onCloseClicked() {
        onComplete()
    }
}