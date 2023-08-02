package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.kizapp.ftpclient.ui.common.LoadingIndicator
import ru.kizapp.ftpclient.ui.common.Toolbar
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.models.RemoteImageContentViewAction
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.models.RemoteImageContentViewEvent

@Composable
fun RemoteImageContentScreen(
    viewModel: RemoteImageContentViewModel,
    filename: String,
) {
    val viewState by viewModel.viewStates().collectAsState()
    val viewAction by viewModel.viewActions().collectAsState(initial = null)
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Toolbar(title = filename) {
            viewModel.obtainEvent(RemoteImageContentViewEvent.OnBackClick)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = viewState.imagePath,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(zoomState = rememberZoomState()),
            )
            if (viewState.loading) {
                LoadingIndicator()
            }
        }
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    LaunchedEffect(key1 = viewAction) {
        when (viewAction) {
            RemoteImageContentViewAction.GoBack -> backPressedDispatcher?.onBackPressed()
            null -> Unit
        }
        viewModel.obtainEvent(RemoteImageContentViewEvent.ClearAction)
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.obtainEvent(RemoteImageContentViewEvent.LoadImage(filename))
    }
}
