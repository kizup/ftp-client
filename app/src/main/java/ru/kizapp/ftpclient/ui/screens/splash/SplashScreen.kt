package ru.kizapp.ftpclient.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.R
import ru.kizapp.ftpclient.navigation.NavigationTree

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel,
) {
    LaunchedEffect(key1 = viewModel) {
        launch {
            delay(500L)
            navController.navigate(NavigationTree.Root.ConnectionList.name) {
                popUpTo(NavigationTree.Root.Splash.name) {
                    inclusive = true
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_folder), contentDescription = null,
            modifier = Modifier.size(64.dp),
        )
        Text(
            text = "FTP Client",
            fontSize = 24.sp,
        )
    }
}
