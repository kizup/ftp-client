package ru.kizapp.ftpclient.data.repository

import ru.kizapp.ftpclient.data.local.db.ConnectionsDao
import ru.kizapp.ftpclient.models.FTPConnection
import javax.inject.Inject

interface FtpConnectionsRepository {

    suspend fun storedConnections(): List<FTPConnection>
    suspend fun saveConnection(connection: FTPConnection)

    suspend fun loadConnection(id: Int): FTPConnection?

    suspend fun deleteConnection(connection: FTPConnection)

    suspend fun updateConnection(connection: FTPConnection)

    class Impl @Inject constructor(
        private val connectionsDao: ConnectionsDao,
    ) : FtpConnectionsRepository {

        override suspend fun storedConnections(): List<FTPConnection> =
            connectionsDao.getSavedConnections()

        override suspend fun saveConnection(connection: FTPConnection) {
            connectionsDao.saveConnection(connection)
        }

        override suspend fun loadConnection(id: Int) = connectionsDao.loadConnection(id)

        override suspend fun deleteConnection(connection: FTPConnection) =
            connectionsDao.deleteConnection(connection)

        override suspend fun updateConnection(connection: FTPConnection) =
            connectionsDao.updateConnection(connection)
    }
}
