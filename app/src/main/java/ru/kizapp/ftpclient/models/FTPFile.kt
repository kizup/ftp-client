package ru.kizapp.ftpclient.models

import androidx.compose.runtime.Stable

@Stable
class FTPFile(
    val fileName: String,
    val fileSize: String,
    val isDir: Boolean,
    val isHidden: Boolean,
    val isNavigateUp: Boolean,
)
