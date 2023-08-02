package ru.kizapp.ftpclient.ui.screens.connection.add.models

sealed class AddConnectionEvent {

    object ClearAction : AddConnectionEvent()
    class OnAddClicked(val connectionId: Int?) : AddConnectionEvent()
    class OnHostChanged(val host: String) : AddConnectionEvent()
    class OnConnectionNameChanged(val name: String) : AddConnectionEvent()
    class OnPortChanged(val port: String) : AddConnectionEvent()
    class OnUserNameChanged(val username: String) : AddConnectionEvent()
    class OnPasswordChanged(val password: String) : AddConnectionEvent()
    class LoadConnection(val editConnectionId: Int?) : AddConnectionEvent()
    object OnBackClick : AddConnectionEvent()
}
