package ru.kizapp.ftpclient.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kizapp.ftpclient.data.local.db.ConnectionsDao
import ru.kizapp.ftpclient.models.FTPConnection
import javax.inject.Inject

interface FtpConnectionsRepository {

    fun storedConnections(): Flow<FTPConnection>
    suspend fun saveConnection(connection: FTPConnection)

    suspend fun loadConnection(id: Int): FTPConnection

    class Impl @Inject constructor(
        private val connectionsDao: ConnectionsDao,
    ) : FtpConnectionsRepository {

        override fun storedConnections(): Flow<FTPConnection> =
            connectionsDao.getSavedConnections()

        override suspend fun saveConnection(connection: FTPConnection) {
            connectionsDao.saveConnection(connection)
        }

        override suspend fun loadConnection(id: Int): FTPConnection =
            connectionsDao.loadConnection(id)
    }
}
