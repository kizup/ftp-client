package ru.kizapp.ftpclient.ui.screens.filelist.remote.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.kizapp.ftpclient.R
import ru.kizapp.ftpclient.models.FTPFile
import ru.kizapp.ftpclient.navigation.NavigationTree
import ru.kizapp.ftpclient.ui.common.LoadingIndicator
import ru.kizapp.ftpclient.ui.common.Toolbar
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models.RemoteFileListAction
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models.RemoteFileListState
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models.RemoteListEvent

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RemoteFileListScreen(
    navController: NavController,
    viewModel: RemoteFileListViewModel,
) {
    val viewState by viewModel.viewStates().collectAsState()
    val viewActions by viewModel.viewActions().collectAsState(initial = null)

    Scaffold(
        topBar = {
            Toolbar(title = "File list") {
                viewModel.obtainEvent(RemoteListEvent.OnCloseClick)
            }
        },
    ) { paddingValues ->
        BackHandler(enabled = true) {
            viewModel.obtainEvent(RemoteListEvent.OnBackClick)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            BreadCrumbs(viewState = viewState, modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                FileList(
                    viewState = viewState,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxHeight(),
                )
                if (viewState.loading) {
                    LoadingIndicator()
                }
            }
        }
    }

    LaunchedEffect(key1 = viewActions) {
        when (viewActions) {
            RemoteFileListAction.GoBack -> navController.navigate(NavigationTree.Root.ConnectionList.name) {
                popUpTo(0)
            }

            is RemoteFileListAction.ShowFileContent -> {
                val file = (viewActions as RemoteFileListAction.ShowFileContent).file
                navController.currentBackStackEntry?.arguments?.putString(
                    NavigationTree.Arguments.Filename.name,
                    file.fileName
                )
                navController.navigate(NavigationTree.Root.RemoteFileContent.name + "/${file.fileName}")
            }

            is RemoteFileListAction.ShowFileImageContent -> {
                val file = (viewActions as RemoteFileListAction.ShowFileImageContent).file
                navController.currentBackStackEntry?.arguments?.putString(
                    NavigationTree.Arguments.Filename.name,
                    file.fileName
                )
                navController.navigate(NavigationTree.Root.RemoteImageContent.name + "/${file.fileName}")
            }

            null -> Unit
        }
        viewModel.obtainEvent(RemoteListEvent.ClearAction)
    }
    LaunchedEffect(key1 = viewModel) {
        viewModel.obtainEvent(RemoteListEvent.Init)
    }
}

@Composable
fun FileList(
    viewState: RemoteFileListState,
    viewModel: RemoteFileListViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = viewState.fileList,
            key = { file -> file.fileName },
        ) { file ->
            Column {
                FileListItem(file = file) { viewModel.obtainEvent(RemoteListEvent.OnFileClick(it)) }
                Divider()
            }
        }
    }
}

@Composable
fun FileListItem(
    file: FTPFile,
    onClick: (FTPFile) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(file) }
            .padding(8.dp)
            .alpha(if (file.isHidden) 0.5f else 1f),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconResource = when {
            file.isDir -> R.drawable.ic_folder
            else -> R.drawable.ic_file
        }
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            modifier = Modifier.size(36.dp),
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = file.fileName)
            if (!file.isDir) {
                Text(text = file.fileSize)
            }
        }
    }
}

@Composable
fun BreadCrumbs(
    viewState: RemoteFileListState,
    modifier: Modifier = Modifier,
) {
    if (viewState.breadcrumbs.isEmpty()) {
        Text(text = "/")
    } else {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
        ) {
            items(
                items = viewState.breadcrumbs,
                key = { value -> value }
            ) { value ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = value,
                        modifier = Modifier
                            .background(Color.Gray.copy(alpha = 0.5f))
                            .clickable { }
                            .padding(4.dp),
                        fontSize = 16.sp,
                    )
                    Text(text = "/")
                }
            }
        }
    }
}
