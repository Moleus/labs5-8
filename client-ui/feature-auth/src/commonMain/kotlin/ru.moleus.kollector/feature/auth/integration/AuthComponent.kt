package ru.moleus.kollector.feature.auth.integration

import ru.moleus.kollector.feature.auth.Authentication
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import common.context.CommandExecutor
import ru.moleus.kollector.feature.auth.store.AuthStore
import ru.moleus.kollector.feature.auth.store.AuthStoreProvider
import ru.moleus.kollector.utils.asValue

class AuthComponent(
    componentContext: ComponentContext,
    commandExecutor: CommandExecutor
) : Authentication, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            AuthStoreProvider(
                storeFactory = DefaultStoreFactory(), commandExecutor = commandExecutor
            ).provide()
        }

    override val model: Value<Authentication.Model> = store.asValue().map(stateToModel)
    override val events: Observable<Authentication.Event> = store.labels.map(labelToEvent)

    override fun onLoginChanged(login: String) {
        store.accept(AuthStore.Intent.SetLogin(login))
    }

    override fun onPasswordChanged(password: String) {
        store.accept(AuthStore.Intent.SetPassword(password))
    }

    override fun onSubmitLoginClicked() {
        store.accept(AuthStore.Intent.SubmitLogin)
    }

    override fun onSubmitRegisterClicked() {
        store.accept(AuthStore.Intent.SubmitRegister)
    }
}