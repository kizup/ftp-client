package ru.kizapp.ftpclient.models

enum class FTPConnectionType(val defaultPort: Int) {
    FTP(defaultPort = 21),
    SFTP(defaultPort = 22),
    FTPS(defaultPort = 990),
    ;
}
