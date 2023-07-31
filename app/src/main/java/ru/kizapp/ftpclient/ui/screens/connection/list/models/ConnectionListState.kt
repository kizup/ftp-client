package ru.kizapp.ftpclient.ui.screens.connection.list.models

import androidx.compose.runtime.Immutable
import ru.kizapp.ftpclient.models.FTPConnection

@Immutable
data class ConnectionListState(
    val connections: List<FTPConnection> = emptyList(),
    val loading: Boolean = true,
)
