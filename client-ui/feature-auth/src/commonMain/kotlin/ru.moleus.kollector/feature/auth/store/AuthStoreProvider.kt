package ru.moleus.kollector.feature.auth.store

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
import ru.moleus.kollector.feature.auth.store.AuthStore.*
import user.User

internal class AuthStoreProvider(
    private val storeFactory: StoreFactory,
    private val commandExecutor: CommandExecutor
) {
    fun provide(): AuthStore =
        object : AuthStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegistrationFormStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    /**
     * Executor communicates with Reducer via this class.
     */
    private sealed interface Msg {
        data class LoginEntered(val login: String) : Msg
        data class PasswordEntered(val password: String) : Msg
        data class Loading(val isLoading: Boolean) : Msg
        data class InvalidCredentials(val errorMsg: String) : Msg
    }

    /**
     * Processes entered login/password and submit button click.
     * Messages are passed to the Reducer.
     * Labels are emitted straight to the outside world.
     */
    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetLogin -> dispatch(Msg.LoginEntered(intent.login))
                is Intent.SetPassword -> dispatch(Msg.PasswordEntered(intent.password))
                is Intent.SubmitLogin -> logIn(getState().login, getState().password)
                is Intent.SubmitRegister -> register(getState().login, getState().password)
            }
        }

        private fun logIn(login: String, password: String) {
            if (validateCredentials(login, password)) {
                val payload = ExecutionPayload.of("login", null)
                    .apply {
                        user = User(login, password.toByteArray())
                    }
                launch { commandExecutor.execute(payload) }
            }
        }

        private fun register(login: String, password: String) {
            if (validateCredentials(login, password)) {
                val payload = ExecutionPayload.of("register", null)
                    .apply {
                        user = User(login, password.toByteArray())
                    }
                launch { commandExecutor.execute(payload) }
            }
        }

        private fun launch(
            unit: () -> ExecutionResult
        ) {
            dispatch(Msg.Loading(true))
            singleFromFunction { unit() }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribeScoped(isThreadLocal = true) {
                    processAuthResult(it)
                }
        }

        private fun processAuthResult(result: ExecutionResult) {
            if (!result.isSuccess) {
                dispatch(Msg.InvalidCredentials(result.message))
            }
        }

        private fun validateCredentials(login: String, password: String): Boolean {
            val minLoginLen = 4
            val minPasswordLen = 4

            when {
                login.length < minLoginLen -> dispatch(Msg.InvalidCredentials(textToShort("Login", minLoginLen)))
                login.containsCyrillic() -> dispatch(Msg.InvalidCredentials(CYRILLIC_IN_LOGIN))
                password.length < minPasswordLen -> dispatch(
                    Msg.InvalidCredentials(
                        textToShort(
                            "Password",
                            minPasswordLen
                        )
                    )
                )
                password.containsCyrillic() -> dispatch(Msg.InvalidCredentials(CYRILLIC_IN_PASS))
                else -> return true
            }
            return false
        }

        private fun String.containsCyrillic(): Boolean = this.contains(Regex("[А-Яа-я]+"))
    }

    /**
     * Accepts messages from executor and creates new State.
     */
    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.LoginEntered -> copy(login = msg.login, isError = false)
                is Msg.PasswordEntered -> copy(password = msg.password, isError = false)
                is Msg.InvalidCredentials -> copy(
                    isError = true,
                    isLoading = false,
                    errorMsg = msg.errorMsg,
                )
                is Msg.Loading -> copy(isLoading = msg.isLoading)
            }
    }

}