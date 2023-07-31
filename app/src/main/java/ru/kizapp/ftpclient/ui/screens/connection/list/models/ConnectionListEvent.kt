package ru.kizapp.ftpclient.ui.screens.connection.list.models

import ru.kizapp.ftpclient.models.FTPConnection

sealed class ConnectionListEvent {
    object Load : ConnectionListEvent()
    class OnConnectionSelected(val connection: FTPConnection) : ConnectionListEvent()
    object ClearAction : ConnectionListEvent()
    object OnAddConnectionClick : ConnectionListEvent()
}