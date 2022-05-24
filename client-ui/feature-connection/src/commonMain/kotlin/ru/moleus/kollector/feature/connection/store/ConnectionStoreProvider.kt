package ru.moleus.kollector.feature.connection.store

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
import common.context.ClientContext
import common.context.Session
import ru.moleus.kollector.feature.connection.store.ConnectionStore.*
import java.io.IOException

class ConnectionStoreProvider(
    private val storeFactory: StoreFactory,
    private val clientSession: Session,
    private val getContext: (Session) -> ClientContext,
    private val onFinished: (ClientContext) -> Unit,

    ) {
    fun provide(): ConnectionStore =
        object : ConnectionStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationFormStore",
            initialState = State("127.0.0.1", "2222"),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    /**
     * Executor communicates with Reducer via this class.
     */
    private sealed interface Msg {
        data class IpEntered(val ip: String) : Msg
        data class PortEntered(val port: String) : Msg
        data class Loading(val isLoading: Boolean) : Msg
    }

    /**
     * Processes entered login/password and submit button click.
     * Messages are passed to the Reducer.
     * Labels are emitted straight to the outside world.
     */
    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetIp -> dispatch(Msg.IpEntered(intent.ip))
                is Intent.SetPort -> onPortEnter(intent.port)
                is Intent.SubmitConnect -> connect(getState().serverIp, getState().serverPort)
                is Intent.FetchFromServer -> fetchFromServer()
            }
        }

        private fun connect(ip: String, port: String) {
            try {
                launch { clientSession.connect(ip, port.toInt()) }
            } catch (e: NumberFormatException) {
                publish(Label.MessageReceived("Port must be an integer."))
            }
        }

        private fun onPortEnter(port: String) {
            dispatch(Msg.PortEntered(port))
        }

        private fun launch(
            unit: () -> Unit
        ) {
            dispatch(Msg.Loading(true))
            singleFromFunction { unit() }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribeScoped(isThreadLocal = true,
                    onSuccess = {
                        dispatch(Msg.Loading(false))
                        fetchFromServer()
                    },
                    onError = {
                        dispatch(Msg.Loading(false))
                        processError(it)
                    }
                )
        }

        private fun fetchFromServer() {
            singleFromFunction { getContext(clientSession) }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribeScoped(isThreadLocal = true,
                    onSuccess = {
                        onFetchFinished(it)
                    },
                    onError = {
                        processError(it)
                    }
                )
        }

        private fun onFetchFinished(clientContext: ClientContext) {
            onFinished(clientContext)
        }

        private fun processError(e: Throwable) {
            when (e) {
                is IllegalArgumentException -> publish(Label.MessageReceived("Invalid ip or port."))
                is IOException -> publish(Label.MessageReceived("Connection failed."))
            }
        }
    }

    /**
     * Accepts messages from executor and creates new State.
     */
    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.IpEntered -> copy(serverIp = msg.ip)
                is Msg.PortEntered -> copy(serverPort = msg.port)
                is Msg.Loading -> copy(isLoading = msg.isLoading)
            }
    }
}