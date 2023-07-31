package ru.kizapp.ftpclient.navigation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.kizapp.ftpclient.ui.screens.connection.add.AddConnectionScreen
import ru.kizapp.ftpclient.ui.screens.connection.add.AddConnectionViewModel
import ru.kizapp.ftpclient.ui.screens.connection.list.ConnectionListViewModel
import ru.kizapp.ftpclient.ui.screens.connection.list.ConnectionsListScreen
import ru.kizapp.ftpclient.ui.screens.filelist.remote.RemoteFileListScreen
import ru.kizapp.ftpclient.ui.screens.filelist.remote.RemoteFileListViewModel
import ru.kizapp.ftpclient.ui.screens.splash.SplashScreen
import ru.kizapp.ftpclient.ui.screens.splash.SplashViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationTree.Root.Splash.name,
            modifier = Modifier.consumeWindowInsets(paddingValues),
        ) {
            composable(NavigationTree.Root.Splash.name) {
                val splashViewModel = hiltViewModel<SplashViewModel>()
                SplashScreen(
                    navController = navController,
                    viewModel = splashViewModel,
                )
            }
            composable(NavigationTree.Root.AddConnection.name) {
                val addConnectionViewModel = hiltViewModel<AddConnectionViewModel>()
                AddConnectionScreen(
                    navController = navController,
                    viewModel = addConnectionViewModel,
                )
            }
            composable(NavigationTree.Root.ConnectionList.name) {
                val connectionListViewModel = hiltViewModel<ConnectionListViewModel>()
                ConnectionsListScreen(
                    navController = navController,
                    viewModel = connectionListViewModel,
                )
            }
            composable(NavigationTree.Root.RemoteFileList.name) {
                val remoteFileListViewModel = hiltViewModel<RemoteFileListViewModel>()
                RemoteFileListScreen(
                    navController = navController,
                    viewModel = remoteFileListViewModel,
                )
            }
        }
    }
}
