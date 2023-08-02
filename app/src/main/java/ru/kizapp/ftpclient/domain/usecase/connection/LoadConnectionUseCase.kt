package ru.kizapp.ftpclient.domain.usecase.connection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kizapp.ftpclient.data.repository.FtpConnectionsRepository
import ru.kizapp.ftpclient.models.FTPConnection
import javax.inject.Inject

class LoadConnectionUseCase @Inject constructor(
    private val connectionsRepository: FtpConnectionsRepository,
) {
    suspend operator fun invoke(id: Int): FTPConnection? {
        return withContext(Dispatchers.IO) {
            connectionsRepository.loadConnection(id)
        }
    }
}
