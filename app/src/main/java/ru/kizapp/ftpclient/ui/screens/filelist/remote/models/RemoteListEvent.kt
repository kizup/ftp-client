package ru.kizapp.ftpclient.ui.screens.filelist.remote.models

import ru.kizapp.ftpclient.models.FTPFile

sealed class RemoteListEvent {
    class OnFileClick(val file: FTPFile) : RemoteListEvent()
    object OnBackClick : RemoteListEvent()
    object Init : RemoteListEvent()
}
