package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.base.BaseViewModel
import ru.kizapp.ftpclient.data.ftp.FTPClientWrapper
import ru.kizapp.ftpclient.domain.usecase.remote.DownloadFileUseCase
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models.RemoteFileContentViewAction
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models.RemoteFileContentViewEvent
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.text.models.RemoteFileContentViewState
import javax.inject.Inject

@HiltViewModel
class RemoteFileContentViewModel @Inject constructor(
    private val downloadFileUseCase: DownloadFileUseCase,
    private val ftpClientWrapper: FTPClientWrapper,
) :
    BaseViewModel<RemoteFileContentViewState, RemoteFileContentViewAction, RemoteFileContentViewEvent>(
        initialState = RemoteFileContentViewState()
    ) {

    private var downloadFileJob: Job? = null

    init {
        listenDownloadFileProgress()
    }

    override fun onCleared() {
        super.onCleared()
        cancelDownloadJob()
    }

    override fun obtainEvent(viewEvent: RemoteFileContentViewEvent) {
        when (viewEvent) {
            RemoteFileContentViewEvent.OnBackClick -> {
                cancelDownloadJob()
                viewAction = RemoteFileContentViewAction.GoBack
            }

            RemoteFileContentViewEvent.StopLoad -> {
                cancelDownloadJob()
                viewState = viewState.copy(loading = false)
                viewAction = RemoteFileContentViewAction.GoBack
            }

            RemoteFileContentViewEvent.ClearAction -> clearActions()

            is RemoteFileContentViewEvent.Load -> loadFileContent(viewEvent.filename)
        }
    }

    private fun loadFileContent(filename: String) {
        downloadFileJob = viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
                viewState = viewState.copy(loading = false)
            }
        ) {
            viewState = viewState.copy(loading = true)
            val content = downloadFileUseCase(filename)
                .bufferedReader()
                .readLines()
            viewState = viewState.copy(
                content = content,
                loading = false,
            )
        }
    }

    private fun listenDownloadFileProgress() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
            }
        ) {
            ftpClientWrapper.downloadProgress.collect { percent ->
                if (viewState.loading) {
                    val percentFloat = percent / 100f
                    viewState = viewState.copy(
                        progressFloat = percentFloat,
                        progressPercent = percent,
                    )
                }
            }
        }
    }

    private fun cancelDownloadJob() {
        downloadFileJob?.cancel()
        downloadFileJob = null
        viewState = viewState.copy(
            progressPercent = 0,
            progressFloat = 0f,
        )
    }
}
