package ru.kizapp.ftpclient.ui.screens.connection.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.navigation.NavigationTree
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListAction
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListEvent

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsListScreen(
    navController: NavController,
    viewModel: ConnectionListViewModel,
) {

    val viewAction by viewModel.viewActions().collectAsState(initial = null)
    val viewState by viewModel.viewStates().collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.obtainEvent(ConnectionListEvent.OnAddConnectionClick) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues),
        ) {
            items(viewState.connections) { connection ->
                ConnectionListItem(item = connection) {
                    viewModel.obtainEvent(
                        ConnectionListEvent.OnConnectionSelected(it)
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = viewAction) {
        when (viewAction) {
            ConnectionListAction.ShowRemoteFileList -> {
                navController.navigate(NavigationTree.Root.RemoteFileList.name)
            }
            ConnectionListAction.ShowAddConnection -> {
                navController.navigate(NavigationTree.Root.AddConnection.name)
            }
            null -> Unit
        }
        viewModel.obtainEvent(ConnectionListEvent.ClearAction)
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.obtainEvent(ConnectionListEvent.Load)
    }
}

@Composable
private fun ConnectionListItem(
    modifier: Modifier = Modifier,
    item: FTPConnection,
    onItemClick: (FTPConnection) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = item.host
        )
        Text(text = item.login.orEmpty())
    }
}
