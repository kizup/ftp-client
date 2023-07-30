package ru.kizapp.ftpclient.data.ftp

import kotlinx.coroutines.flow.Flow
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.models.FTPFile

interface FTPClientWrapper {

    val isConnected: Boolean

    val files: Flow<List<FTPFile>>
    val path: Flow<List<String>>

    suspend fun connect(connection: FTPConnection)

    suspend fun listFiles(dir: String? = null)

    suspend fun disconnect()
}
