package ru.moleus.kollector.feature.builder.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import commands.ExecutionPayload
import commands.ExecutionResult
import common.context.CommandExecutor
import exceptions.ValueConstraintsException
import exceptions.ValueFormatException
import model.ModelDto
import model.builder.BuilderWrapper
import ru.moleus.kollector.feature.builder.store.EditorStore.*
import ru.moleus.kollector.feature.builder.util.toFieldInfo
import ru.moleus.kollector.feature.builder.util.toFieldPairs
import ru.moleus.kollector.feature.builder.util.withoutErrors

internal class EditorStoreProvider(
    private val storeFactory: StoreFactory,
    private val dtoBuilder: BuilderWrapper<ModelDto>,
    private val executionPayload: ExecutionPayload,
    private val commandExecutor: CommandExecutor,
    private val initialValues: Map<String, String>,
) {
    fun provide(): EditorStore =
        object : EditorStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationEditorStore",
            initialState = State(filledValues = initialValues.toFieldInfo()),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    /**
     * Executor communicates with Reducer via this class.
     */
    private sealed interface Msg {
        data class Loading(val isLoading: Boolean) : Msg
        data class ValueEntered(val label: String, val value: String) : Msg
        data class InvalidValue(val label: String, val errorMsg: String) : Msg
        data class SubmitStatus(val state: Boolean) : Msg
        data class ToolbarVisible(val isVisible: Boolean) : Msg
        object ClearErrors : Msg
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
                is Intent.SetToolbarVisible -> dispatch(Msg.ToolbarVisible(intent.isToolbarVisible))
            }.let { }
        }

        private fun setValue(label: String, value: String) {
            dispatch(Msg.ValueEntered(label, value))
        }

        private fun submit(state: State) {
            dispatch(Msg.ClearErrors)
            mapToDto(state.filledValues.toFieldPairs())
            println("Building dto from: ${state.filledValues.toFieldPairs()}")
        }

        private fun mapToDto(values: Map<String, String>) {
            dtoBuilder.position = 0
            var isSuccess = true

            for ((label, value) in values.entries) {
                try {
                    dtoBuilder.setValue(value)
                } catch (e: Exception) {
                    when (e) {
                        is ValueConstraintsException,
                        is ValueFormatException -> {
                            dispatch(Msg.InvalidValue(label, e.message.orEmpty()))
                            isSuccess = false
                        }

                        else -> throw e
                    }
                }
                dtoBuilder.step()
            }
            if (isSuccess) {
                executionPayload.data = dtoBuilder.build()
                launch {
                    commandExecutor.execute(executionPayload)
                }
            }
        }

        private fun launch(
            request: () -> ExecutionResult,
        ) {
            dispatch(Msg.Loading(true))
            singleFromFunction { request() }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribeScoped(isThreadLocal = true,
                    onSuccess = {
                        dispatch(Msg.Loading(false))
                        processResult(it)
                    },
                    onError = {
                        processError(it)
                    })
        }

        private fun processResult(result: ExecutionResult) {
            dispatch(Msg.SubmitStatus(result.isSuccess))
            //TODO: send notification to user.
            publish(Label.MessageReceived(result.message))
        }

        private fun processError(e: Throwable) {
            e.message?.let {
                publish(Label.MessageReceived(it))
            }
        }
    }

    /**
     * Accepts messages from executor and creates new State.
     */
    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.InvalidValue -> {
                    copy(filledValues = filledValues.map { field ->
                        field.takeIf { it.label == msg.label }?.copy(errorMsg = msg.errorMsg) ?: field
                    })
                }
                is Msg.ClearErrors -> copy(filledValues = filledValues.withoutErrors())
                is Msg.ValueEntered -> copy(
                    filledValues = filledValues.map { field ->
                        field.takeIf { it.label == msg.label }?.copy(value = msg.value) ?: field
                    }
                )
                is Msg.SubmitStatus -> copy(isSuccess = msg.state)
                is Msg.Loading -> copy(isLoading = true, isError = false)
                is Msg.ToolbarVisible -> copy(isToolbarVisible = msg.isVisible)
            }
    }
}
