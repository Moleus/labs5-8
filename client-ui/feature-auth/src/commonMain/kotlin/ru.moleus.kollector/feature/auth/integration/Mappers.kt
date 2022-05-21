package ru.moleus.kollector.feature.auth.integration

import ru.moleus.kollector.feature.auth.Authentication
import ru.moleus.kollector.feature.auth.store.AuthStore

internal val stateToModel: (AuthStore.State) -> (Authentication.Model) =
    {
        Authentication.Model(
            login = it.login,
            password = it.password,
            isLoading = it.isLoading,
        )
    }

internal val labelToEvent: (AuthStore.Label) -> Authentication.Event =
    {
        when (it) {
            is AuthStore.Label.MessageReceived -> Authentication.Event.MessageReceived(message = it.message)
        }
    }