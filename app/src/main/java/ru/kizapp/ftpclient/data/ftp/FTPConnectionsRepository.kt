package ru.kizapp.ftpclient.data.ftp

import ru.kizapp.ftpclient.models.FTPConnection

interface FTPConnectionsRepository {

    suspend fun addFtpConnection(connection: FTPConnection)

    suspend fun loadFtpConnections(): List<FTPConnection>

    class Impl : FTPConnectionsRepository {

        private val connections by lazy { ArrayList<FTPConnection>() }

        override suspend fun addFtpConnection(connection: FTPConnection) {
            connections.add(connection)
        }

        override suspend fun loadFtpConnections(): List<FTPConnection> {
            return connections
        }
    }
}
