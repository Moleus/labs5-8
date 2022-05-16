package my.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import common.root.ui.RootUi
import root.RootComponent
import root.app.DefaultClientContext
import root.app.MockAuthenticator
import root.app.MockDtoBuilder
import root.app.MockEntityProvider
import ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val clientContext = initClientContext()

        val lifecycle = LifecycleRegistry()
        val root =
            RootComponent(
                componentContext = DefaultComponentContext(lifecycle),
                clientContext = clientContext
            )
        setContent {
            MyTheme {
                Surface(color = MaterialTheme.colors.background) {
                    RootUi(root = root)
                }
            }
        }
    }
}

fun initClientContext() =
    DefaultClientContext(MockAuthenticator(), MockEntityProvider(), MockDtoBuilder())
