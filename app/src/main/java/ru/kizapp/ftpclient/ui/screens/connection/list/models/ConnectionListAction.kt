package ru.kizapp.ftpclient.ui.screens.connection.list.models

sealed class ConnectionListAction {
    object ShowRemoteFileList : ConnectionListAction()
    object ShowAddConnection : ConnectionListAction()
    class ShowEditConnection(val connectionId: Int) : ConnectionListAction()
}
