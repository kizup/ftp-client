package ru.kizapp.ftpclient.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.kizapp.ftpclient.ui.screens.connection.add.AddConnectionScreen
import ru.kizapp.ftpclient.ui.screens.connection.add.AddConnectionViewModel
import ru.kizapp.ftpclient.ui.screens.connection.list.ConnectionListViewModel
import ru.kizapp.ftpclient.ui.screens.connection.list.ConnectionsListScreen
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.RemoteImageContentScreen
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.RemoteImageContentViewModel
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.RemoteFileContentScreen
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.RemoteFileContentViewModel
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.RemoteFileListScreen
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.RemoteFileListViewModel
import ru.kizapp.ftpclient.ui.screens.splash.SplashScreen
import ru.kizapp.ftpclient.ui.screens.splash.SplashViewModel

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavigationTree.Root.Splash.name,
    ) {
        composable(route = NavigationTree.Root.Splash.name) {
            val splashViewModel = hiltViewModel<SplashViewModel>()
            SplashScreen(
                navController = navController,
                viewModel = splashViewModel,
            )
        }
        composable(route = NavigationTree.Root.AddConnection.name) {
            val addConnectionViewModel = hiltViewModel<AddConnectionViewModel>()
            AddConnectionScreen(
                navController = navController,
                viewModel = addConnectionViewModel,
            )
        }
        composable(route = NavigationTree.Root.ConnectionList.name) {
            val connectionListViewModel = hiltViewModel<ConnectionListViewModel>()
            ConnectionsListScreen(
                navController = navController,
                viewModel = connectionListViewModel,
            )
        }
        composable(route = NavigationTree.Root.RemoteFileList.name) {
            val remoteFileListViewModel = hiltViewModel<RemoteFileListViewModel>()
            RemoteFileListScreen(
                navController = navController,
                viewModel = remoteFileListViewModel,
            )
        }
        composable(
            route = NavigationTree.Root.RemoteFileContent.name + "/{${NavigationTree.Arguments.Filename.name}}",
            arguments = listOf(
                navArgument(
                    NavigationTree.Arguments.Filename.name
                ) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val remoteFileContentViewModel = hiltViewModel<RemoteFileContentViewModel>()
            RemoteFileContentScreen(
                navController = navController,
                viewModel = remoteFileContentViewModel,
                filename = backStackEntry.arguments?.getString(NavigationTree.Arguments.Filename.name)
                    .orEmpty(),
            )
        }
        composable(
            route = NavigationTree.Root.RemoteImageContent.name + "/{${NavigationTree.Arguments.Filename.name}}",
            arguments = listOf(
                navArgument(
                    NavigationTree.Arguments.Filename.name
                ) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val remoteImageViewModel = hiltViewModel<RemoteImageContentViewModel>()
            RemoteImageContentScreen(
                viewModel = remoteImageViewModel,
                filename = backStackEntry.arguments?.getString(NavigationTree.Arguments.Filename.name)
                    .orEmpty(),
            )
        }
    }
}
