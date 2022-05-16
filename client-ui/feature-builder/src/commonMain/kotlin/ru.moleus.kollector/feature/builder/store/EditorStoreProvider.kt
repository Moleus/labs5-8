package ru.moleus.kollector.feature.builder.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.single.singleTimer
import data.DtoBuilder
import ru.moleus.kollector.feature.builder.store.EditorStore.*

internal class EditorStoreProvider(
    private val storeFactory: StoreFactory,
    private val dtoBuilder: DtoBuilder,
    private val initialValues: Map<String, String>
) {
    fun provide(): EditorStore =
        object : EditorStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationEditorStore",
            initialState = State(initialValues),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = EditorStoreProvider::ExecutorImpl,
            reducer = ReducerImpl
        ) {}


    /**
     * Executor communicates with Reducer via this class.
     */
    private sealed interface Msg {
        data class ValueEntered(val label: String, val value: String) : Msg
        data class InvalidValue(val errorMsg: String) : Msg
        data class SubmitStatus(val state: Boolean) : Msg
        object SubmitClicked : Msg
    }

    /**
     * Processes entered values and submit button click.
     * Messages are passed to the Reducer.
     * Labels are emitted straight to the outside world.
     */
    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetValue -> setValue(label = intent.label, value = intent.value)
                is Intent.Submit -> submit(state = getState())
            }
        }

        private fun setValue(label: String, value: String) {
            dtoBuilder.setValue(label, value)
            dispatch(Msg.ValueEntered(label, value))
        }

        private fun submit(state: State) {
            //TODO create separate methods for Add/Update submit or check enum?
            // send command for execution.
            println("Building dto from: ${state.filledValues.entries}")
//            singleFromFunction { dtoBuilder.build().register(state.login, state.password) }
//                .subscribeOn(computationScheduler)
//                .subscribeScoped { processAuthResult(it) }
        }

        private fun submitSuccessWithTimeout(){
            dispatch(Msg.SubmitStatus(true))
            singleTimer(3000, computationScheduler).subscribeScoped { dispatch(Msg.SubmitStatus(false)) }
        }
    }

    /**
     * Accepts messages from executor and creates new State.
     */
    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.InvalidValue -> copy(isError = true, errorMsg = msg.errorMsg)
                is Msg.ValueEntered -> copy(filledValues = filledValues + (msg.label to msg.value))
                is Msg.SubmitClicked -> copy(isLoading = true, isError = false)
                is Msg.SubmitStatus -> copy(isSuccess = msg.state)
            }
    }

}
