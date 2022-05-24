package ru.moleus.kollector.feature.overview.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.decompose.value.reduce
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import common.context.ClientContext

class OverviewComponent(
    componentContext: ComponentContext,
    clientContext: ClientContext,
) : Overview, ComponentContext by componentContext {
    private val _model = MutableValue(Overview.Model())
    override val model: Value<Overview.Model> = _model

    private val isDetailsToolbarVisible = BehaviorSubject(!_model.value.isMultiPane)
    private val selectedEntityIdSubject = BehaviorSubject<Long?>(null)

    private val tableRouter =
        TableRouter(
            componentContext = this,
            entityProvider = clientContext,
            selectedEntityId = selectedEntityIdSubject,
            onEntitySelected = ::onEntitySelected
        )

    override val tableRouterState: Value<RouterState<*, Overview.TableChild>> = tableRouter.state

    private val detailsRouter =
        DetailsRouter(
            componentContext = this,
            clientContext = clientContext,
            isToolbarVisible = isDetailsToolbarVisible,
            onFinished = ::closeDetailsAndShowTable
        )

    override val detailsRouterState: Value<RouterState<*, Overview.DetailsChild>> = detailsRouter.state

    init {
        detailsRouter.state.observe(lifecycle) {
            selectedEntityIdSubject.onNext(it.activeChild.configuration.getEntityId())
        }
    }

    private fun closeDetailsAndShowTable() {
        tableRouter.show()
        detailsRouter.closeDetailsPane()
    }

    private fun onEntitySelected(id: Long) {
        detailsRouter.showEntity(id = id)

        if (isMultiPaneMode()) {
            tableRouter.show()
        } else {
            tableRouter.moveToBackStack()
        }
    }

    override fun setMultiPane(isMultiPane: Boolean) {
        _model.reduce { it.copy(isMultiPane = isMultiPane) }
        isDetailsToolbarVisible.onNext(!isMultiPane)

        if (isMultiPane) {
            switchToMultiPane()
        } else {
            switchToSinglePane()
        }
    }

    private fun switchToMultiPane() {
        tableRouter.show()
    }

    private fun switchToSinglePane() {
        if (detailsRouter.isShown()) {
            tableRouter.moveToBackStack()
        } else {
            tableRouter.show()
        }
    }

    private fun isMultiPaneMode(): Boolean = _model.value.isMultiPane

    private fun DetailsRouter.Config.getEntityId(): Long? =
        when (this) {
            is DetailsRouter.Config.None -> null
            is DetailsRouter.Config.Details -> entityId
            is DetailsRouter.Config.Updater -> entityId
        }
}