package ru.kizapp.ftpclient.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kizapp.ftpclient.models.FTPConnection

@Database(
    entities = [
        FTPConnection::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class FtpDb : RoomDatabase() {
    abstract fun connectionsDao(): ConnectionsDao
}
