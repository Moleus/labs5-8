package common.root.ui


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.*
import common.main.ui.MainUi
import common.root.Root

@ExperimentalComposeUiApi
@ExperimentalDecomposeApi
@Composable
fun RootUi(root: Root) {
    Children(
        routerState = root.routerState,
        animation = childAnimation(fade() + scale())
    ) {
        when (val child = it.instance) {
            is Root.Child.MainChild -> MainUi(child.component, modifier = Modifier.fillMaxSize())
        }
    }
}
