package ru.kizapp.ftpclient.ui.screens.connection.add.models

import androidx.compose.runtime.Immutable
import ru.kizapp.ftpclient.models.FTPConnectionType

@Immutable
data class AddConnectionState(
    val hostEmpty: Boolean = false,
    val loading: Boolean = false,
    val connectionType: FTPConnectionType = FTPConnectionType.FTP,
    val connectionName: String? = null,
    val connectionHost: String = "",
    val connectionPort: Int = connectionType.defaultPort,
    val userName: String? = null,
    val password: String? = null,
    val editConnection: Boolean = false,
)
