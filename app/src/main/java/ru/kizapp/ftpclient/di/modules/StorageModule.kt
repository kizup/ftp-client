package ru.kizapp.ftpclient.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kizapp.ftpclient.data.local.db.ConnectionsDao
import ru.kizapp.ftpclient.data.local.db.FtpDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, FtpDb::class.java, "ftp_db")
        .build()

    @Provides
    fun provideConnectionsDao(db: FtpDb): ConnectionsDao = db.connectionsDao()
}
