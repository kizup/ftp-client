package ru.kizapp.ftpclient.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kizapp.ftpclient.models.FTPConnection

@Dao
interface ConnectionsDao {

    @Query("select * from ftp_connections")
    suspend fun getSavedConnections(): List<FTPConnection>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveConnection(item: FTPConnection)

    @Query("select * from ftp_connections where id = :id")
    suspend fun loadConnection(id: Int): FTPConnection
}
