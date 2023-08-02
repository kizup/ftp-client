package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.models

sealed class RemoteImageContentViewEvent {

    class LoadImage(val filename: String) : RemoteImageContentViewEvent()
    object ClearAction : RemoteImageContentViewEvent()
    object OnBackClick : RemoteImageContentViewEvent()
}
