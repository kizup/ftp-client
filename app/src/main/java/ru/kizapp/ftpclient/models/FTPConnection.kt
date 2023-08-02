package ru.kizapp.ftpclient.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.kizapp.ftpclient.data.local.db.Converters

@Entity(tableName = "ftp_connections")
@TypeConverters(Converters::class)
class FTPConnection(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "type")
    val type: FTPConnectionType,
    @ColumnInfo(name = "host")
    val host: String,
    @ColumnInfo(name = "port")
    val port: Int = type.defaultPort,
    // TODO move credentials to another table
    @ColumnInfo(name = "login")
    val login: String? = null,
    // TODO store password is not secure
    @ColumnInfo(name = "password")
    val password: String? = null,
    @ColumnInfo("connection_name")
    val name: String? = null,
)
