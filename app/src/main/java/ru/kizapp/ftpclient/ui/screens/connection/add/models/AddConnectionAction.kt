package ru.kizapp.ftpclient.ui.screens.connection.add.models

sealed class AddConnectionAction {
    object HideKeyboard : AddConnectionAction()
    object ShowConnectionsList : AddConnectionAction()
    object GoBack : AddConnectionAction()
}
