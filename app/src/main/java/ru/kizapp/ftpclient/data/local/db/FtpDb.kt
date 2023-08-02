package ru.kizapp.ftpclient.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kizapp.ftpclient.models.FTPConnection

@Database(
    entities = [
        FTPConnection::class,
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(1, 2),
    ]
)
abstract class FtpDb : RoomDatabase() {
    abstract fun connectionsDao(): ConnectionsDao
}
