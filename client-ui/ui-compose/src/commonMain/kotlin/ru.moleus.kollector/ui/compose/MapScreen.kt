package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import root.EntitiesMap
//import store.MapStore

@Composable
fun MapUi(component: EntitiesMap, modifier: Modifier) {
//    val model by component.model.subscribeAsState()
//    MapContainer(model, modifier.fillMaxSize())
}

@Composable
fun MapContainer(
//    model : MapStore,
    modifier: Modifier = Modifier
) {
//    MapUI(modifier, state = model.mapState)
}
