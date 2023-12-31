package ru.kizapp.ftpclient.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.data.ftp.impl.FTPClientWrapperImpl
import ru.kizapp.ftpclient.data.repository.FtpConnectionsRepository

@Module
@InstallIn(SingletonComponent::class)
interface BindingModule {

    @Binds
    fun bindConnectionRepository(impl: FtpConnectionsRepository.Impl): FtpConnectionsRepository

    @Binds
    fun bindFTPClientWrapper(impl: FTPClientWrapperImpl): FTPClientWrapper
}
