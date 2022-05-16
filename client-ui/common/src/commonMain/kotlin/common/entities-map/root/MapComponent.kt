package root

import data.EntityProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
//import store.MapStore
import java.io.File
import java.io.FileInputStream

class MapComponent(
    componentContext: ComponentContext,
    private val entityProvider: EntityProvider
) : EntitiesMap, ComponentContext by componentContext {
//    override val model: Value<MapStore> = MutableValue(MapStore())

}