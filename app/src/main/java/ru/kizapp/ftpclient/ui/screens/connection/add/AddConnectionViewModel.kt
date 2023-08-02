package ru.kizapp.ftpclient.ui.screens.connection.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.base.BaseViewModel
import ru.kizapp.ftpclient.domain.usecase.connection.AddConnectionUseCase
import ru.kizapp.ftpclient.domain.usecase.connection.LoadConnectionUseCase
import ru.kizapp.ftpclient.domain.usecase.connection.UpdateConnectionUseCase
import ru.kizapp.ftpclient.models.FTPConnection
import ru.kizapp.ftpclient.models.FTPConnectionType
import ru.kizapp.ftpclient.ui.screens.connection.add.models.AddConnectionAction
import ru.kizapp.ftpclient.ui.screens.connection.add.models.AddConnectionEvent
import ru.kizapp.ftpclient.ui.screens.connection.add.models.AddConnectionState
import javax.inject.Inject

@HiltViewModel
class AddConnectionViewModel @Inject constructor(
    private val addConnectionUseCase: AddConnectionUseCase,
    private val loadConnectionUseCase: LoadConnectionUseCase,
    private val updateConnectionUseCase: UpdateConnectionUseCase,
) : BaseViewModel<AddConnectionState, AddConnectionAction, AddConnectionEvent>(
    AddConnectionState()
) {
    override fun obtainEvent(viewEvent: AddConnectionEvent) {
        when (viewEvent) {
            AddConnectionEvent.ClearAction -> clearActions()
            is AddConnectionEvent.OnAddClicked -> onAddConnectionClicked(viewEvent.connectionId)
            AddConnectionEvent.OnBackClick -> {
                viewAction = AddConnectionAction.GoBack
            }

            is AddConnectionEvent.OnHostChanged -> {
                viewState = viewState.copy(connectionHost = viewEvent.host)
            }

            is AddConnectionEvent.OnPortChanged -> {
                val port = viewEvent.port.toIntOrNull() ?: viewState.connectionType.defaultPort
                if (port <= 0 || port > 65536) {
                    viewState = viewState
                    return
                }
                viewState = viewState.copy(connectionPort = port)
            }

            is AddConnectionEvent.OnConnectionNameChanged -> {
                viewState = viewState.copy(connectionName = viewEvent.name)
            }

            is AddConnectionEvent.OnUserNameChanged -> {
                viewState = viewState.copy(userName = viewEvent.username)
            }

            is AddConnectionEvent.OnPasswordChanged -> {
                viewState = viewState.copy(password = viewEvent.password)
            }

            is AddConnectionEvent.LoadConnection -> {
                loadConnection(viewEvent.editConnectionId)
            }
        }
    }

    private fun onAddConnectionClicked(connectionId: Int?) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
            }
        ) {
            viewAction = AddConnectionAction.HideKeyboard
            viewState = viewState.copy(loading = true)

            if (connectionId == null) {
                // TODO add fields validation
                val connection = FTPConnection(
                    // TODO Add connection type selection
                    type = FTPConnectionType.SFTP,
                    host = viewState.connectionHost,
                    login = viewState.userName,
                    password = viewState.password,
                )
                addConnectionUseCase(connection)
            } else {
                val connection = loadConnectionUseCase(connectionId)
                if (connection != null) {
                    val updatedConnection = connection.copy(
                        name = viewState.connectionName,
                        host = viewState.connectionHost,
                        password = viewState.password,
                        login = viewState.userName,
                    )
                    updateConnectionUseCase(updatedConnection)
                }
            }
            viewState = viewState.copy(loading = false)
            viewAction = AddConnectionAction.ShowConnectionsList
        }
    }

    private fun loadConnection(editConnectionId: Int?) {
        viewState = viewState.copy(editConnection = editConnectionId != null)
        if (editConnectionId == null) {
            return
        }
        viewModelScope.launch(
            Dispatchers.IO + CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
                viewState = viewState.copy(loading = false)
            }
        ) {
            viewState = viewState.copy(loading = true)
            val connection = loadConnectionUseCase(editConnectionId)

            if (connection == null) {
                // TODO show failed load message
            } else {
                viewState = viewState.copy(
                    connectionName = connection.name,
                    connectionPort = connection.port,
                    connectionHost = connection.host,
                    userName = connection.login,
                    password = connection.password,
                )
            }
            viewState = viewState.copy(loading = false)
        }
    }
}
