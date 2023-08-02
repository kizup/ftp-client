package ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models

import ru.kizapp.ftpclient.models.FTPFile

sealed class RemoteFileListAction {
    object GoBack : RemoteFileListAction()
    class ShowFileContent(val file: FTPFile) : RemoteFileListAction()
    class ShowFileImageContent(val file: FTPFile) : RemoteFileListAction()
}
