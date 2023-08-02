package ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models

import ru.kizapp.ftpclient.models.FTPFile

sealed class RemoteListEvent {
    class OnFileClick(val file: FTPFile) : RemoteListEvent()
    object OnBackClick : RemoteListEvent()
    object OnCloseClick : RemoteListEvent()
    object Init : RemoteListEvent()
    object ClearAction : RemoteListEvent()
}
