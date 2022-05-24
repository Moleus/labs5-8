package ru.moleus.kollector.feature.connection.util

import ru.moleus.kollector.feature.connection.Connection
import ru.moleus.kollector.feature.connection.store.ConnectionStore

internal val labelToEvent: (ConnectionStore.Label) -> Connection.Event =
    {
        when (it) {
            is ConnectionStore.Label.MessageReceived -> Connection.Event.MessageReceived(message = it.message)
        }
    }
