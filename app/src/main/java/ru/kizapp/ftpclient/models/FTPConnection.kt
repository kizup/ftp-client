package ru.kizapp.ftpclient.models

class FTPConnection(
    val type: FTPConnectionType,
    val host: String,
    val port: Int = type.defaultPort,
    val login: String? = null,
    val password: CharArray? = null,
)
