package ru.kizapp.ftpclient.ui.screens.connection.list.models

import ru.kizapp.ftpclient.models.FTPConnection

sealed class ConnectionListEvent {
    object Load : ConnectionListEvent()
    class OnConnectionClick(val connection: FTPConnection) : ConnectionListEvent()
    class OnDeleteConnectionClick(val connection: FTPConnection) : ConnectionListEvent()
    class OnEditConnectionClick(val connection: FTPConnection) : ConnectionListEvent()
    object ClearAction : ConnectionListEvent()
    object OnAddConnectionClick : ConnectionListEvent()
    object DismissConfirmationAlert : ConnectionListEvent()
    class DeleteConnectionConfirmed(val connection: FTPConnection) : ConnectionListEvent()
}