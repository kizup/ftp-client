package ru.kizapp.ftpclient.ui.screens.filelist.remote.models

import ru.kizapp.ftpclient.models.FTPFile

data class RemoteFileListState(
    val fileList: List<FTPFile> = emptyList(),
    val breadcrumbs: List<String> = emptyList(),
    val loading: Boolean = true,
)
