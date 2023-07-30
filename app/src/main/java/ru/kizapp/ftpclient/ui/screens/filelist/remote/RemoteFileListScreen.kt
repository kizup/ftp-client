package ru.kizapp.ftpclient.ui.screens.filelist.remote

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.R
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.models.FTPFile

@Composable
fun RemoteFileListScreen(
    ftpClient: FTPClientWrapper,
) {
    val scope = rememberCoroutineScope()
    Column {
        BreadCrumbs(ftpClient = ftpClient, modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        FileList(
            ftpClient = ftpClient,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        ftpClient.disconnect()
                    }
                }
            ) {
                Text(text = "Disconnect")
            }
        }
    }
}


@Composable
fun FileList(ftpClient: FTPClientWrapper, modifier: Modifier = Modifier) {
    val files = ftpClient.files.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = files.value,
            key = { file -> file.fileName },
        ) { file ->
            Column {
                FileListItem(file = file) {
                    if (file.isDir) {
                        scope.launch(
                            Dispatchers.IO + CoroutineExceptionHandler { _, t ->
                                t.printStackTrace()

                            }
                        ) {
                            ftpClient.listFiles(file.fileName)
                        }
                    }
                }
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
    ftpClient: FTPClientWrapper,
    modifier: Modifier = Modifier,
) {
    val path = ftpClient.path.collectAsState(initial = emptyList())
    LazyRow(
        modifier = modifier.fillMaxWidth(),
    ) {
        items(
            items = path.value,
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
