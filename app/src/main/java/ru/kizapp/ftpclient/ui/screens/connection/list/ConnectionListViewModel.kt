package ru.kizapp.ftpclient.ui.screens.connection.list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.base.BaseViewModel
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.domain.usecase.connection.DeleteConnectionUseCase
import ru.kizapp.ftpclient.domain.usecase.connection.FetchSavedConnectionsUseCase
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListAction
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListEvent
import ru.kizapp.ftpclient.ui.screens.connection.list.models.ConnectionListState
import javax.inject.Inject

@HiltViewModel
class ConnectionListViewModel @Inject constructor(
    private val fetchSavedConnectionsUseCase: FetchSavedConnectionsUseCase,
    private val deleteConnectionUseCase: DeleteConnectionUseCase,
    private val ftpClientWrapper: FTPClientWrapper,
) : BaseViewModel<ConnectionListState, ConnectionListAction, ConnectionListEvent>(
    ConnectionListState()
) {
    override fun obtainEvent(viewEvent: ConnectionListEvent) {
        when (viewEvent) {
            ConnectionListEvent.Load -> loadConnections()
            ConnectionListEvent.ClearAction -> clearActions()
            is ConnectionListEvent.OnAddConnectionClick -> onAddConnectionClicked()
            is ConnectionListEvent.OnConnectionClick -> onConnectClicked(viewEvent.connection)
            is ConnectionListEvent.OnDeleteConnectionClick ->
                viewState = viewState.copy(connectionForDelete = viewEvent.connection)

            is ConnectionListEvent.OnEditConnectionClick -> viewAction =
                ConnectionListAction.ShowEditConnection(viewEvent.connection.id)

            is ConnectionListEvent.DeleteConnectionConfirmed -> {
                viewState = viewState.copy(connectionForDelete = null)
                onDeleteConnectionClicked(viewEvent.connection)
            }

            is ConnectionListEvent.DismissConfirmationAlert -> {
                viewState = viewState.copy(connectionForDelete = null)
            }
        }
    }

    private fun loadConnections() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO add logging
                t.printStackTrace()
                viewState = viewState.copy(loading = false)
            }
        ) {
            viewState = viewState.copy(loading = true)
            val connections = fetchSavedConnectionsUseCase()
            // TODO check if connections is empty
            viewState = viewState.copy(
                connections = connections,
                loading = false,
            )
        }
    }

    private fun onAddConnectionClicked() {
        viewModelScope.launch {
            viewAction = ConnectionListAction.ShowAddConnection
        }
    }

    private fun onConnectClicked(connection: FTPConnection) {
        viewModelScope.launch(
            Dispatchers.IO + CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
                viewModelScope.launch(Dispatchers.IO) { ftpClientWrapper.disconnect() }
            }
        ) {
            viewState = viewState.copy(loading = true)
            ftpClientWrapper.connect(connection)
            viewState = viewState.copy(loading = false)
            viewAction = ConnectionListAction.ShowRemoteFileList
        }
    }

    private fun onDeleteConnectionClicked(connection: FTPConnection) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO handle error
                t.printStackTrace()
                viewState = viewState.copy(loading = false)
            }
        ) {
            viewState = viewState.copy(loading = true)
            deleteConnectionUseCase(connection)
            viewState = viewState.copy(loading = false)
            obtainEvent(ConnectionListEvent.Load)
        }
    }
}
