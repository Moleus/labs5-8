package registration.form.integration

import auth.Authenticator
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import common.registration.form.integration.labelToEvent
import common.registration.form.integration.stateToModel
import registration.form.Authentication
import common.registration.form.store.AuthStore
import common.registration.form.store.AuthStoreProvider
import common.shared.util.asValue

class AuthComponent(
    componentContext: ComponentContext,
    authenticator: Authenticator
) : Authentication, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            AuthStoreProvider(
                storeFactory = DefaultStoreFactory(), authenticator = authenticator
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