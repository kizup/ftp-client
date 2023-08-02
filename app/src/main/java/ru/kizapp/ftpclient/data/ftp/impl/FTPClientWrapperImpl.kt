package ru.kizapp.ftpclient.data.ftp.impl

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import ru.kizapp.ftpclient.data.ftp.DownloadFileMonitor
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.models.FTPConnectionType
import ru.kizapp.ftpclient.models.FTPFile
import ru.kizapp.ftpclient.models.exceptions.FailedToConnectException
import ru.kizapp.ftpclient.utils.getFormattedFileSize
import java.io.File
import java.nio.channels.NotYetConnectedException
import java.util.Properties
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class FTPClientWrapperImpl @Inject constructor(
    private val downloadFileMonitor: DownloadFileMonitor,
) : FTPClientWrapper {

    private var sftpChannel: ChannelSftp? = null
    private var sftpSession: Session? = null

    private var activeConnection: FTPConnection? = null
    private var ftpClient: FTPClient? = null

    private val jschClient by lazy { JSch() }
    private val lockObject by lazy { Any() }

    private var currentDirectory: String? = null
    private val filesMutableFlow by lazy { MutableStateFlow<List<FTPFile>>(emptyList()) }
    private val pathMutableFlow by lazy { MutableStateFlow<List<String>>(emptyList()) }

    override val isConnected: Boolean
        get() = synchronized(lockObject) {
            activeConnection != null && (sftpSession?.isConnected ?: false)
        }

    override val files: Flow<List<FTPFile>>
        get() = filesMutableFlow

    override val path: Flow<List<String>>
        get() = pathMutableFlow

    override val downloadProgress: Flow<Int>
        get() = downloadFileMonitor.percentFlow

    override suspend fun connect(connection: FTPConnection) =
        suspendCancellableCoroutine { continuation ->
            if (isConnected && continuation.isActive) {
                continuation.resumeWithException(IllegalStateException("Already connected to ${activeConnection?.host}"))
                return@suspendCancellableCoroutine
            }
            when (connection.type) {
                FTPConnectionType.SFTP -> connectToSftp(connection, continuation)
                else -> continuation.resumeWithException(NotImplementedError("No implementation for ${connection.type} connection type"))
            }
        }

    override suspend fun listFiles(dir: String?) {
        if (!isConnected) {
            return
        }
        withContext(Dispatchers.IO) {
            val files = when (val type = activeConnection?.type) {
                FTPConnectionType.SFTP -> listFilesSftp(dir)
                else -> throw NotImplementedError("No implementation for $type connection type")
            }
            filesMutableFlow.emit(files)
        }
    }

    override suspend fun navigateUp(): Boolean = withContext(Dispatchers.IO) {
        if (workDirectoryPaths.isNotEmpty()) {
            listFiles(NAVIGATE_UP)
            true
        } else {
            false
        }
    }

    override suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            if (!isConnected) {
                return@withContext
            }
            disconnectFromSftp()
            pathMutableFlow.emit(emptyList())
            filesMutableFlow.emit(emptyList())
            workDirectoryPaths.clear()
            activeConnection = null
            currentDirectory = null
        }
    }

    override suspend fun downloadFile(filename: String, target: File) =
        suspendCancellableCoroutine { continuation ->
            val channel = sftpChannel
            if (channel == null) {
                continuation.resumeWithException(NullPointerException("Channel is null"))
                return@suspendCancellableCoroutine
            }
            val src = getCurrentDirectory() + filename
            continuation.invokeOnCancellation { downloadFileMonitor.cancel() }
            channel.get(src, target.outputStream(), downloadFileMonitor)
            if (continuation.isActive) {
                continuation.resumeWith(Result.success(Unit))
            }
        }

    private fun connectToSftp(
        connection: FTPConnection,
        continuation: CancellableContinuation<Unit>
    ) {
        val session = jschClient.getSession(connection.login, connection.host, connection.port)
        session.setConfig(
            // TODO remove workaround on UnknownHostKey, need to add host to known hosts via jsch.setKnownHosts()
            Properties().apply {
                put("StrictHostKeyChecking", "no")
            }
        )
        connection.password?.let { password ->
            session.setPassword(password)
        }
        if (!continuation.isActive) {
            session.disconnect()
            return
        }
        session.connect()
        if (!session.isConnected) {
            continuation.resumeWithException(FailedToConnectException())
            return
        }
        sftpSession = session
        sftpChannel = session.openChannel(SFTP_CHANNEL) as ChannelSftp
        sftpChannel?.connect()
        activeConnection = connection
        continuation.resumeWith(Result.success(Unit))
    }

    private fun disconnectFromSftp() {
        sftpChannel?.disconnect()
        sftpSession?.disconnect()
        sftpChannel = null
        sftpSession = null
    }

    private suspend fun listFilesSftp(dir: String?): List<FTPFile> =
        suspendCancellableCoroutine { continuation ->
            val channel = sftpChannel
            if (channel == null) {
                continuation.resumeWithException(NotYetConnectedException())
                return@suspendCancellableCoroutine
            }
            if (currentDirectory == null && continuation.isActive) {
                currentDirectory = channel.pwd()
                updateWorkDirectory()
            }
            val currentDir = dir ?: currentDirectory
            if (currentDir == null) {
                continuation.resumeWithException(IllegalStateException("Cannot get current directory"))
                return@suspendCancellableCoroutine
            }

            when (dir) {
                NAVIGATE_UP -> {
                    if (workDirectoryPaths.isNotEmpty()) {
                        workDirectoryPaths.removeLast()
                    } else {
                        return@suspendCancellableCoroutine
                    }
                }

                null -> Unit
                else -> workDirectoryPaths += dir
            }

            if (!continuation.isActive) {
                return@suspendCancellableCoroutine
            }

            pathMutableFlow.tryEmit(ArrayList(workDirectoryPaths))
            channel.cd(getCurrentDirectory())
            var entries = channel.ls(getCurrentDirectory())
                .filterIsInstance<LsEntry>()
                .filterNot { it.filename == "." }
                .map { entry ->
                    val isSymlink = entry.attrs.isLink
                    val fileName = entry.filename
                    FTPFile(
                        fileName = fileName,
                        fileSize = getFormattedFileSize(entry.attrs.size),
                        isDir = entry.attrs.isDir || isSymlink,
                        isHidden = fileName != NAVIGATE_UP && fileName.startsWith("."),
                        isNavigateUp = fileName == NAVIGATE_UP,
                    )
                }
            if (workDirectoryPaths.isEmpty() && entries.isNotEmpty()) {
                entries = entries.toMutableList().apply { removeIf { it.fileName == ".." } }
            }

            if (entries.isEmpty()) {
                entries = listOf(
                    FTPFile(
                        fileName = NAVIGATE_UP,
                        fileSize = getFormattedFileSize(0L),
                        isDir = true,
                        isNavigateUp = true,
                        isHidden = false,
                    )
                )
            }

            val sortedDirs = entries.filter { it.isDir }.sortedBy { it.fileName.lowercase() }
            val sortedFiles = entries.filterNot { it.isDir }.sortedBy { it.fileName.lowercase() }
            continuation.resumeWith(Result.success(sortedDirs + sortedFiles))
        }

    private fun updateWorkDirectory() {
        val pwd = currentDirectory ?: return
        workDirectoryPaths.clear()
        workDirectoryPaths += pwd.split(UNIX_SEPARATOR).filter { it.isNotEmpty() }
    }

    private fun getCurrentDirectory(): String {
        if (workDirectoryPaths.isEmpty()) {
            return UNIX_SEPARATOR
        }
        return workDirectoryPaths.joinToString(
            separator = UNIX_SEPARATOR,
            postfix = UNIX_SEPARATOR,
            prefix = UNIX_SEPARATOR
        )
    }

    private val workDirectoryPaths = ArrayList<String>()

    private companion object {
        const val SFTP_CHANNEL = "sftp"
        const val UNIX_SEPARATOR = "/"
        const val NAVIGATE_UP = ".."
    }
}
