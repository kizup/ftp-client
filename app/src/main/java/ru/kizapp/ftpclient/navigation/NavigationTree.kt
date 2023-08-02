package ru.kizapp.ftpclient.navigation

object NavigationTree {

    enum class Root {
        Splash, AddConnection, ConnectionList, RemoteFileList, RemoteFileContent, RemoteImageContent,
        ;
    }

    enum class Arguments {
        Filename,
        ConnectionId,
        ;
    }
}
