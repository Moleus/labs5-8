package ru.moleus.kollector.feature.map.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.disposable.scope.DisposableScope
import common.context.EntityProvider
import ru.moleus.kollector.feature.map.EntitiesMap
import ru.moleus.kollector.feature.map.store.MapStore
import ru.moleus.kollector.feature.map.store.MapStoreProvider
import ru.moleus.kollector.utils.asValue
import ru.moleus.kollector.utils.disposableScope

class MapComponent(
    private val componentContext: ComponentContext,
    entityProvider: EntityProvider
) : EntitiesMap, ComponentContext by componentContext, DisposableScope by componentContext.disposableScope() {

    private val store =
        instanceKeeper.getStore {
            MapStoreProvider(
                storeFactory = DefaultStoreFactory()
            ).provide()
        }

    override val model: Value<EntitiesMap.Model> = store.asValue().map(stateToModel)

    init {
        entityProvider.subscribe {
            store.accept(MapStore.Intent.Update(it))
        }
    }
}