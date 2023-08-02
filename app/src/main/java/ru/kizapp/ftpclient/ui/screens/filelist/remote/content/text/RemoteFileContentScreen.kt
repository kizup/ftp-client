package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.kizapp.ftpclient.ui.common.Toolbar
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models.RemoteFileContentViewAction
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models.RemoteFileContentViewEvent
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models.RemoteFileContentViewState

@Composable
fun RemoteFileContentScreen(
    navController: NavController,
    viewModel: RemoteFileContentViewModel,
    filename: String,
) {
    val viewAction by viewModel.viewActions().collectAsState(initial = null)
    val viewState by viewModel.viewStates().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Toolbar(title = "File content") {
            viewModel.obtainEvent(RemoteFileContentViewEvent.OnBackClick)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FileContentList(viewState)
            if (viewState.loading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = viewState.progressFloat,
                    )
                    Text(text = "${viewState.progressPercent}%", modifier = Modifier.padding(8.dp))
                    TextButton(onClick = { viewModel.obtainEvent(RemoteFileContentViewEvent.StopLoad) }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    LaunchedEffect(key1 = viewAction) {
        when (viewAction) {
            RemoteFileContentViewAction.GoBack -> {
                backPressedDispatcher?.onBackPressed()
            }

            null -> Unit
        }
        viewModel.obtainEvent(RemoteFileContentViewEvent.ClearAction)
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.obtainEvent(RemoteFileContentViewEvent.Load(filename))
    }
}

@Composable
private fun FileContentList(viewState: RemoteFileContentViewState) {
    val scroll = rememberScrollState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scroll)
    ) {
        itemsIndexed(
            items = viewState.content,
        ) { index, line -> FileContentLine(index = index, line = line) }
    }
}

@Composable
private fun FileContentLine(index: Int, line: String) {
    val backgroundColor = if (index % 2 > 0) {
        Color.White
    } else {
        Color.Gray.copy(0.2f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            modifier = Modifier
                .background(backgroundColor)
                .padding(4.dp)
                .width(52.dp),
            text = (index + 1).toString(),
            style = TextStyle(
                color = Color.Red,
                fontFamily = FontFamily.Monospace,
            ),
            fontSize = 14.sp,
            textAlign = TextAlign.End,
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = Color.Red.copy(alpha = 0.2f),
        )
        Text(
            modifier = Modifier
                .wrapContentWidth(unbounded = true)
                .padding(4.dp),
            text = line,
            style = TextStyle(fontFamily = FontFamily.Monospace),
            fontSize = 16.sp,
        )
    }
}
