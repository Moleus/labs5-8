package ru.moleus.kollector.feature.map.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.data.Flat
import ru.moleus.kollector.feature.map.store.MapStore.*

internal class MapStoreProvider(
    private val storeFactory: StoreFactory,
) {
    fun provide(): MapStore =
        object : MapStore, Store<Intent, State, Any> by storeFactory.create(
            name = "RegistrationFormStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class UpdateCollection(val entities: Set<Flat>) : Msg
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Any>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.Update -> dispatch(Msg.UpdateCollection(entities = intent.collection))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdateCollection -> copy(entities = msg.entities)
            }
    }
}
