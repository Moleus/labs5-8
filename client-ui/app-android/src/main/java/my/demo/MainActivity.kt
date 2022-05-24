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
import common.context.ClientContext
import common.context.Session
import ru.moleus.kollector.domain.bootsrap.ClientBootstrapper
import ru.moleus.kollector.domain.communication.ConnectionSession
import ru.moleus.kollector.feature.root.RootComponent
import ru.moleus.kollector.ui.compose.RootUi
import ru.moleus.kollector.ui.compose.theme.MyTheme

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val lifecycle = LifecycleRegistry()
        val clientSession = ConnectionSession()
        val root =
            RootComponent(
                componentContext = DefaultComponentContext(lifecycle),
                clientSession = clientSession,
                getContext = ::initContext
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

private fun initContext(clientSession: Session): ClientContext = ClientBootstrapper.initClientContext(clientSession)
