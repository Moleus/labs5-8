package overview.root

import ClientContext
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.decompose.value.reduce
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import common.`entities-overview`.overview.root.DetailsRouter
import common.`entities-overview`.overview.root.TableRouter

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
            entityProvider = clientContext,
            isToolbarVisible = isDetailsToolbarVisible,
            onFinished = ::closeDetailsAndShowTable
        )

    override val detailsRouterState: Value<RouterState<*, Overview.DetailsChild>> = detailsRouter.state

    init {
        backPressedHandler.register {
            if (isMultiPaneMode() || !detailsRouter.isShown()) {
                false
            } else {
                closeDetailsAndShowTable()
                true
            }
        }

        detailsRouter.state.observe(lifecycle) {
            selectedEntityIdSubject.onNext(it.activeChild.configuration.getEntityId())
        }
    }

    private fun closeDetailsAndShowTable() {
        tableRouter.show()
        detailsRouter.closeEntity()
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
        }
}