package ru.kizapp.ftpclient.domain.usecase.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import java.io.File
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(
    private val ftpClient: FTPClientWrapper,
) {
    suspend operator fun invoke(filename: String): File {
        return withContext(Dispatchers.IO) {
            val outputFile = File.createTempFile(filename, "")
            ftpClient.downloadFile(filename, outputFile)
            outputFile
        }
    }
}
