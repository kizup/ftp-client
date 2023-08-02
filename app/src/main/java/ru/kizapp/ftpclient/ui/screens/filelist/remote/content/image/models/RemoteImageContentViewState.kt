package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.models

import androidx.compose.runtime.Stable

@Stable
data class RemoteImageContentViewState(
    val imagePath: String? = null,
    val loading: Boolean = true,
)
