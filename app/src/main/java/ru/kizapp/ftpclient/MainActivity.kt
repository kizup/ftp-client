package ru.kizapp.ftpclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.data.ftp.impl.FTPClientWrapperImpl
import ru.kizapp.ftpclient.presentation.screens.AddConnectionScreen
import ru.kizapp.ftpclient.presentation.screens.RemoteFileListScreen
import ru.kizapp.ftpclient.presentation.screens.SplashScreen
import ru.kizapp.ftpclient.ui.theme.FTPClientTheme

class MainActivity : ComponentActivity() {

    private val ftpClient: FTPClientWrapper by lazy { FTPClientWrapperImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FTPClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") { SplashScreen(navController = navController) }
                        composable("remote-file-list") { RemoteFileListScreen(ftpClient = ftpClient) }
                        composable("add-connection") { AddConnectionScreen() }
                    }
                }
            }
        }
    }
}
