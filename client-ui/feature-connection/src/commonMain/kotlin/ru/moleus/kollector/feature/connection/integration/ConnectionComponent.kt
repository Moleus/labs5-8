package ru.moleus.kollector.feature.connection.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import common.context.ClientContext
import common.context.Session
import ru.moleus.kollector.feature.connection.Connection
import ru.moleus.kollector.feature.connection.store.ConnectionStore
import ru.moleus.kollector.feature.connection.store.ConnectionStoreProvider
import ru.moleus.kollector.feature.connection.util.labelToEvent
import ru.moleus.kollector.utils.asValue

class ConnectionComponent(
    private val componentContext: ComponentContext,
    private val clientSession: Session,
    private val getContext: (Session) -> ClientContext,
    private val onFinished: (ClientContext) -> Unit
) : Connection, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            ConnectionStoreProvider(
                storeFactory = DefaultStoreFactory(),
                clientSession = clientSession,
                getContext = getContext,
                onFinished = onFinished
            ).provide()
        }

    override val model: Value<Connection.Model> = store.asValue().map {
        Connection.Model(
            serverIp = it.serverIp,
            serverPort = it.serverPort,
            isLoading = false
        )
    }

    override val events: Observable<Connection.Event> = store.labels.map(labelToEvent)

    override fun onIpChanged(ip: String) {
        store.accept(ConnectionStore.Intent.SetIp(ip))
    }

    override fun onPortChanged(port: String) {
        store.accept(ConnectionStore.Intent.SetPort(port))
    }

    override fun onConnectClicked() {
        store.accept(ConnectionStore.Intent.SubmitConnect)
    }
}