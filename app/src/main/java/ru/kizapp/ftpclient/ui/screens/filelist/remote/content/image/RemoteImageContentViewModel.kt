package ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.kizapp.ftpclient.base.BaseViewModel
import ru.kizapp.ftpclient.domain.usecase.remote.DownloadFileUseCase
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.models.RemoteImageContentViewAction
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.models.RemoteImageContentViewEvent
import ru.kizapp.ftpclient.ui.screens.filelist.remote.content.image.models.RemoteImageContentViewState
import javax.inject.Inject

@HiltViewModel
class RemoteImageContentViewModel @Inject constructor(
    private val downloadFileUseCase: DownloadFileUseCase,
) :
    BaseViewModel<RemoteImageContentViewState, RemoteImageContentViewAction, RemoteImageContentViewEvent>(
        initialState = RemoteImageContentViewState()
    ) {
    override fun obtainEvent(viewEvent: RemoteImageContentViewEvent) {
        when (viewEvent) {
            RemoteImageContentViewEvent.OnBackClick -> viewAction = RemoteImageContentViewAction.GoBack
            RemoteImageContentViewEvent.ClearAction -> clearActions()
            is RemoteImageContentViewEvent.LoadImage -> loadImageFile(viewEvent.filename)
        }
    }

    private fun loadImageFile(filename: String) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, t ->
                // TODO add error handling
                t.printStackTrace()
                viewState = viewState.copy(loading = false)
            }
        ) {
            viewState = viewState.copy(loading = true)
            val file = downloadFileUseCase(filename)
            viewState = viewState.copy(
                imagePath = file.absolutePath,
                loading = false,
            )
        }
    }
}
