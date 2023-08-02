package ru.kizapp.ftpclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import ru.kizapp.ftpclient.navigation.NavigationGraph
import ru.kizapp.ftpclient.ui.theme.FrontEnd
import ru.kizapp.ftpclient.ui.theme.FrontEndTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                val systemUiController = rememberSystemUiController()
                val backgroundPrimary = FrontEnd.color.backgroundPrimary
                val isLight = FrontEnd.color.isLight
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = backgroundPrimary,
                        darkIcons = isLight,
                    )
                }

                val snackbarState = remember { SnackbarHostState() }

                LaunchedEffect(key1 = "Non") {
                    delay(2000)
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarState) }
                ) { paddingValues ->
                    NavigationGraph()
                }
            }
        }
    }
}
