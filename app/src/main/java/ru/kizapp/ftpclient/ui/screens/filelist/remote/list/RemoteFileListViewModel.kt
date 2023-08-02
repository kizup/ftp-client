package ru.kizapp.ftpclient.ui.screens.filelist.remote.list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.base.BaseViewModel
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.models.FTPFile
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models.RemoteFileListAction
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models.RemoteFileListState
import ru.kizapp.ftpclient.ui.screens.filelist.remote.list.models.RemoteListEvent
import javax.inject.Inject

@HiltViewModel
class RemoteFileListViewModel @Inject constructor(
    private val ftpClient: FTPClientWrapper,
) : BaseViewModel<RemoteFileListState, RemoteFileListAction, RemoteListEvent>(
    initialState = RemoteFileListState()
) {

    private var listenPathsJob: Job? = null
    private var listenFilesJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        cancelJobs()
    }

    override fun obtainEvent(viewEvent: RemoteListEvent) {
        when (viewEvent) {
            is RemoteListEvent.OnFileClick -> onFileClicked(viewEvent.file)
            RemoteListEvent.ClearAction -> clearActions()
            RemoteListEvent.OnBackClick -> onBackClicked()
            RemoteListEvent.OnCloseClick -> onCloseClicked()
            RemoteListEvent.Init -> {
                listenFileListChanges()
                listenPathsChanges()
                loadFileList()
            }
        }
    }

    private fun cancelJobs() {
        listenFilesJob?.cancel()
        listenPathsJob?.cancel()

        listenFilesJob = null
        listenPathsJob = null
    }

    private fun onFileClicked(file: FTPFile) {
        when {
            file.isDir -> {
                viewModelScope.launch(
                    Dispatchers.IO + CoroutineExceptionHandler { _, t ->
                        t.printStackTrace()

                    }
                ) {
                    viewState = viewState.copy(loading = true)
                    ftpClient.listFiles(file.fileName)
                }
            }

            !file.isDir -> {
                viewAction = when {
                    file.fileName.endsWith(".png", ignoreCase = true)
                            or file.fileName.endsWith(".jpg", ignoreCase = true)
                            or file.fileName.endsWith(".jpeg", ignoreCase = true) -> {
                        RemoteFileListAction.ShowFileImageContent(file)
                    }

                    else -> {
                        RemoteFileListAction.ShowFileContent(file)
                    }
                }
            }
        }
    }

    private fun onCloseClicked() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
            }
        ) {
            viewState = viewState.copy(loading = true)
            ftpClient.disconnect()
            viewState = viewState.copy(loading = false)
            viewAction = RemoteFileListAction.GoBack
        }
    }

    private fun onBackClicked() {
        if (viewState.loading) {
            return
        }
        viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
            }
        ) {
            viewState = viewState.copy(loading = true)
            if (!ftpClient.navigateUp()) {
                ftpClient.disconnect()
                cancelJobs()
                viewAction = RemoteFileListAction.GoBack
            }
        }
    }

    private fun listenFileListChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            ftpClient.files.collect { files ->
                viewState = viewState.copy(fileList = files)
                viewState = viewState.copy(loading = false)
            }
        }
    }

    private fun listenPathsChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            ftpClient.path.collect { path ->
                viewState = viewState.copy(breadcrumbs = path)
            }
        }
    }

    private fun loadFileList() {
        viewModelScope.launch(
            Dispatchers.IO + CoroutineExceptionHandler { _, t ->
                t.printStackTrace()
                viewState = viewState.copy(loading = false)
                // TODO add error handling
            }
        ) { ftpClient.listFiles() }
    }
}
