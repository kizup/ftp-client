package ru.kizapp.ftpclient.domain.usecase.connection

import ru.kizapp.ftpclient.data.repository.FtpConnectionsRepository
import ru.kizapp.ftpclient.models.FTPConnection
import javax.inject.Inject

class AddConnectionUseCase @Inject constructor(
    private val repository: FtpConnectionsRepository,
) {

    suspend operator fun invoke(connection: FTPConnection) {
        repository.saveConnection(connection)
    }
}
