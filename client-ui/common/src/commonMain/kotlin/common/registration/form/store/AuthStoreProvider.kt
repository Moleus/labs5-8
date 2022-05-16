package common.registration.form.store

import auth.Authenticator
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import auth.RegistrationStatus
import common.registration.form.store.AuthStore.*
import registration.form.store.CYRILLIC_IN_LOGIN
import registration.form.store.CYRILLIC_IN_PASS
import registration.form.store.textToShort

internal class AuthStoreProvider(
    private val storeFactory: StoreFactory,
    private val authenticator: Authenticator
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
        data class InvalidCredentials(val errorMsg: String) : Msg
        object RegistrationSuccess : Msg
        object SubmitClicked : Msg
        object LoginTimedOut : Msg
    }

    /**
     * Processes entered login/password and submit button click.
     * Messages are passed to the Reducer.
     * Labels are emitted straight to the outside world.
     */
    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetLogin -> setLogin(login = intent.login)
                is Intent.SetPassword -> setPassword(password = intent.password)
                is Intent.SubmitLogin -> submitLogin(state = getState())
                is Intent.SubmitRegister -> submitRegister(state = getState())
            }
        }

        private fun setLogin(login: String) {
            if (containsCyrillic(login)) {
                dispatch(Msg.InvalidCredentials(CYRILLIC_IN_LOGIN))
                return
            }
            dispatch(Msg.LoginEntered(login))
        }

        private fun setPassword(password: String) {
            if (containsCyrillic(password)) {
                dispatch(Msg.InvalidCredentials(CYRILLIC_IN_PASS))
                return
            }
            dispatch(Msg.PasswordEntered(password))
        }

        private fun submitLogin(state: State) {
            if (validateCredentials(state)) {
                singleFromFunction { authenticator.login(state.login, state.password) }
                    .subscribeOn(computationScheduler)
                    .subscribeScoped { processAuthResult(it) }
            }
        }

        private fun submitRegister(state: State) {
            if (validateCredentials(state)) {
                dispatch(Msg.LoginTimedOut)
//                singleFromFunction { authenticator.register(state.login, state.password) }
//                    .subscribeOn(computationScheduler)
//                    .observeOn(mainScheduler)
//                    .subscribeScoped { processAuthResult(it) }
//
            }
        }

        private fun processAuthResult(result: RegistrationStatus) {
            when (result) {
                RegistrationStatus.INVALID_CREDENTIALS -> dispatch(
                    Msg.InvalidCredentials("Invalid login or password")
                )
                RegistrationStatus.ALREADY_EXISTS -> dispatch(
                    Msg.InvalidCredentials("Account already exists")
                )
                RegistrationStatus.SUCCESS -> setLoggedInWithTimeOut()
            }
        }

        private fun setLoggedInWithTimeOut(){
            dispatch(Msg.RegistrationSuccess)
//            singleTimer(3000, computationScheduler).subscribeScoped { dispatch(Msg.LoginTimedOut) }
        }

        private fun validateCredentials(state: State): Boolean {
            val minLoginLen = 4
            val minPasswordLen = 4

            if (state.login.length < minLoginLen) {
                // login should be longer than 3 symbols error
                dispatch(Msg.InvalidCredentials(textToShort("Login", minLoginLen)))
                return false
            }
            if (state.password.length < minPasswordLen) {
                dispatch(Msg.InvalidCredentials(textToShort("Password", minPasswordLen)))
                return false
            }
            dispatch(Msg.SubmitClicked)
            return true
        }

        private fun containsCyrillic(text: String): Boolean = text.contains(Regex("[А-Яа-я]+"))
    }

    /**
     * Accepts messages from executor and creates new State.
     */
    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.LoginEntered -> copy(login = msg.login, isError = false, isLoggedIn = false)
                is Msg.PasswordEntered -> copy(password = msg.password, isError = false, isLoggedIn = false)
                is Msg.InvalidCredentials -> copy(isError = true, isLoading = false, errorMsg = msg.errorMsg, isLoggedIn = false)
                is Msg.SubmitClicked -> copy(isLoading = true, isError = false, isLoggedIn = false)
                is Msg.RegistrationSuccess -> copy(isError = false, isLoading = false, isLoggedIn = true)
                is Msg.LoginTimedOut -> copy(isLoggedIn = false)
            }
    }

}