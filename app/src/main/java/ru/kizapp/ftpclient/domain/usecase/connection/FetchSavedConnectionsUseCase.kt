package ru.kizapp.ftpclient.domain.usecase.connection

import ru.kizapp.ftpclient.data.repository.FtpConnectionsRepository
import javax.inject.Inject

class FetchSavedConnectionsUseCase @Inject constructor(
    private val ftpConnectionsRepository: FtpConnectionsRepository,
) {
    suspend operator fun invoke() = ftpConnectionsRepository.storedConnections()
}
