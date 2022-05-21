package ru.moleus.kollector.feature.builder.store

import com.arkivanov.mvikotlin.core.store.*
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
import model.builder.ModelDtoBuilderWrapper
import model.data.ModelDtoBuilder
import ru.moleus.kollector.feature.builder.store.EditorStore.*
import ru.moleus.kollector.feature.builder.FieldInfo
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
            initialState = State(initialValues.toFieldInfo()),
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
            }
        }

        private fun setValue(label: String, value: String) {
            dispatch(Msg.ValueEntered(label, value))
        }

        private fun submit(state: State) {
            dispatch(Msg.ClearErrors)
            // Map state.filledValues.entries to modelDto using dtoBuilder
            // call lambda with passed modelDto.
            // в абстрактный
            // onSubmit(modelDto)
            mapToDto(state.filledValues.toFieldPairs())
            //TODO create separate methods for Add/Update submit or check enum?
            // send command for execution.
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
                .subscribeScoped(isThreadLocal = true) {
                    dispatch(Msg.Loading(false))
                    processResult(it)
                }
        }

        private fun processResult(result: ExecutionResult) {
            dispatch(Msg.SubmitStatus(result.isSuccess))
            //TODO: send notification to user.
        }
    }

    /**
     * Accepts messages from executor and creates new State.
     */
    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.InvalidValue -> copy(filledValues = filledValues.map {
                    if (it.label == msg.label) it.copy(
                        errorMsg = msg.errorMsg
                    ) else it
                }.toSet())
                is Msg.ClearErrors -> copy(filledValues = filledValues.withoutErrors() )
                is Msg.ValueEntered -> copy(
                    filledValues = filledValues + FieldInfo(
                        label = msg.label,
                        value = msg.value
                    )
                )
                is Msg.SubmitStatus -> copy(isSuccess = msg.state)
                is Msg.Loading -> copy(isLoading = true, isError = false)
            }
    }
}
