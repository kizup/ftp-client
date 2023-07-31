package ru.kizapp.ftpclient.ui.screens.connection.list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.base.BaseViewModel
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.domain.usecase.connection.FetchSavedConnectionsUseCase
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListAction
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListEvent
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListState
import javax.inject.Inject

@HiltViewModel
class ConnectionListViewModel @Inject constructor(
    private val fetchSavedConnectionsUseCase: FetchSavedConnectionsUseCase,
    private val ftpClientWrapper: FTPClientWrapper,
) : BaseViewModel<ConnectionListState, ConnectionListAction, ConnectionListEvent>(
    ConnectionListState()
) {
    override fun obtainEvent(viewEvent: ConnectionListEvent) {
        clearActions()
        when (viewEvent) {
            ConnectionListEvent.Load -> loadConnections()
            ConnectionListEvent.ClearAction -> clearActions()
            ConnectionListEvent.OnAddConnectionClick -> onAddConnectionClicked()
            is ConnectionListEvent.OnConnectionSelected -> onConnectClicked(viewEvent.connection)
        }
    }

    private fun loadConnections() {
        viewModelScope.launch {
            viewState = viewState.copy(
                loading = true,
            )
            try {
                val connections = fetchSavedConnectionsUseCase()
                // TODO check if connections is empty
                viewState = viewState.copy(
                    connections = connections,
                    loading = false,
                )
            } catch (e: Exception) {
                viewState = viewState.copy(loading = false)
                // TODO add logging and error handling
                e.printStackTrace()
            }
        }
    }

    private fun onAddConnectionClicked() {
        viewModelScope.launch {
            viewAction = ConnectionListAction.ShowAddConnection
        }
    }

    private fun onConnectClicked(connection: FTPConnection) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ftpClientWrapper.connect(connection)
                viewAction = ConnectionListAction.ShowRemoteFileList
            } catch (e: Exception) {
                ftpClientWrapper.disconnect()
                // TODO handle error
                e.printStackTrace()
            }
        }
    }
}
