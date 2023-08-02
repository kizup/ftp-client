package ru.kizapp.ftpclient.data.ftp

import kotlinx.coroutines.flow.Flow
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.models.FTPFile
import java.io.File
import java.io.InputStream

interface FTPClientWrapper {

    val isConnected: Boolean

    val files: Flow<List<FTPFile>>
    val path: Flow<List<String>>
    val downloadProgress: Flow<Int>

    suspend fun connect(connection: FTPConnection)

    suspend fun listFiles(dir: String? = null)

    suspend fun navigateUp(): Boolean

    suspend fun disconnect()

    suspend fun downloadFile(filename: String, target: File)
}
