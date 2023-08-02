package ru.kizapp.ftpclient.ui.screens.connection.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.kizapp.ftpclient.R
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.models.FTPConnectionType
import ru.kizapp.ftpclient.navigation.NavigationTree
import ru.kizapp.ftpclient.ui.common.LoadingIndicator
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
        Box(modifier = Modifier.consumeWindowInsets(paddingValues)) {
            ConnectionList(connections = viewState.connections) { event ->
                viewModel.obtainEvent(event)
            }
            if (viewState.loading) {
                LoadingIndicator()
            }

            val connectionForDelete = viewState.connectionForDelete
            if (connectionForDelete != null) {
                DeleteConfirmationDialog(connectionForDelete, viewModel)
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
private fun DeleteConfirmationDialog(
    connectionForDelete: FTPConnection,
    viewModel: ConnectionListViewModel
) {
    AlertDialog(
        onDismissRequest = {
            viewModel.obtainEvent(ConnectionListEvent.DismissConfirmationAlert)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.obtainEvent(
                        ConnectionListEvent.DeleteConnectionConfirmed(
                            connectionForDelete
                        )
                    )
                }
            ) {
                Text(text = "Delete")
            }
        },
        title = { Text(text = "Warning") },
        text = { Text(text = "Are you want to delete connection?") },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.obtainEvent(ConnectionListEvent.DismissConfirmationAlert)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
private fun ConnectionList(
    connections: List<FTPConnection>,
    onEventEmitted: (ConnectionListEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        items(connections) { connection ->
            ConnectionListItem(
                item = connection,
                onItemClick = { item ->
                    onEventEmitted(ConnectionListEvent.OnConnectionClick(item))
                },
                onDeleteClick = { item ->
                    onEventEmitted(ConnectionListEvent.OnDeleteConnectionClick(item))
                }
            )
        }
    }

}

@Composable
private fun ConnectionListItem(
    modifier: Modifier = Modifier,
    item: FTPConnection,
    onItemClick: (FTPConnection) -> Unit,
    onDeleteClick: (FTPConnection) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onItemClick(item) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ftp),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Text(text = item.host)
                    Text(text = item.login.orEmpty())
                }
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onDeleteClick(item) }
                        .padding(4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConnectionListPreview() {
    ConnectionList(
        connections = listOf(
            FTPConnection(
                type = FTPConnectionType.FTP,
                host = "demo.host",
                port = 21,
                login = null,
                password = "",
            ),
            FTPConnection(
                type = FTPConnectionType.FTP,
                host = "192.168.0.1",
                port = 21,
                login = "localhost",
                password = "",
            )
        ),
        onEventEmitted = {},
    )
}
