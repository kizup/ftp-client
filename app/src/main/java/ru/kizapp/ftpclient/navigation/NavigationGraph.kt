package ru.kizapp.ftpclient.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.kizapp.ftpclient.ui.screens.connection.add.AddConnectionScreen
import ru.kizapp.ftpclient.ui.screens.splash.SplashScreen
import ru.kizapp.ftpclient.ui.screens.splash.SplashViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    Scaffold { paddingValues ->
        paddingValues.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = NavigationTree.Root.Splash.name,
        ) {
            composable(NavigationTree.Root.Splash.name) {
                val splashViewModel = hiltViewModel<SplashViewModel>()
                SplashScreen(navController = navController, viewModel = splashViewModel)
            }
            composable(NavigationTree.Root.AddConnection.name) {
                AddConnectionScreen(navController = navController)
            }
        }
    }
}
