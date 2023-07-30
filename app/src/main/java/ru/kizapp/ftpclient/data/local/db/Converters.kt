package ru.kizapp.ftpclient.data.local.db

import androidx.room.TypeConverter
import ru.kizapp.ftpclient.models.FTPConnectionType

class Converters {
    @TypeConverter
    fun fromConnectionType(type: FTPConnectionType) = type.ordinal

    @TypeConverter
    fun toConnectionType(ordinal: Int) = FTPConnectionType.values()[ordinal]
}
