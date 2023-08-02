package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models

sealed class RemoteFileContentViewEvent {

    class Load(val filename: String) : RemoteFileContentViewEvent()
    object OnBackClick : RemoteFileContentViewEvent()
    object StopLoad : RemoteFileContentViewEvent()
    object ClearAction : RemoteFileContentViewEvent()
}
