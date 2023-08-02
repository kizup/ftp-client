package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models

data class RemoteFileContentViewState(
    val content: List<String> = emptyList(),
    val loading: Boolean = true,
    val progressFloat: Float = 0f,
    val progressPercent: Int = 0,
)
