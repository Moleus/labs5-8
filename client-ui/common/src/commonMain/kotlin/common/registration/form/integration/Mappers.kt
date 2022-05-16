package common.registration.form.integration

import common.registration.form.store.AuthStore
import registration.form.Authentication


internal val stateToModel: (AuthStore.State) -> (Authentication.Model) =
    {
        Authentication.Model(
            login = it.login,
            password = it.password,
            isLoading = it.isLoading,
            isLoggedIn = it.isLoggedIn,
        )
    }

internal val labelToEvent: (AuthStore.Label) -> Authentication.Event =
    {
        when (it) {
            is AuthStore.Label.MessageReceived -> Authentication.Event.MessageReceived(message = it.message)
        }
    }