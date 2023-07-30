package ru.kizapp.ftpclient.domain.usecase

import ru.kizapp.ftpclient.data.repository.FtpConnectionsRepository
import javax.inject.Inject

class FetchSavedConnectionsUseCase @Inject constructor(
    private val ftpConnectionsRepository: FtpConnectionsRepository,
) {
    operator fun invoke() = ftpConnectionsRepository.storedConnections()
}
