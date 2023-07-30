package ru.kizapp.ftpclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.data.ftp.impl.FTPClientWrapperImpl
import ru.kizapp.ftpclient.navigation.NavigationGraph
import ru.kizapp.ftpclient.ui.screens.connection.add.AddConnectionScreen
import ru.kizapp.ftpclient.ui.screens.filelist.remote.RemoteFileListScreen
import ru.kizapp.ftpclient.ui.screens.splash.SplashScreen
import ru.kizapp.ftpclient.ui.theme.FrontEnd
import ru.kizapp.ftpclient.ui.theme.FrontEndTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
                NavigationGraph()
            }
        }
    }
}
