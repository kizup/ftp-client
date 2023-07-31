package ru.kizapp.ftpclient.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<State : Any, Action, Event>(initialState: State) : ViewModel() {

    private val _viewStates by lazy { MutableStateFlow(initialState) }
    private val _viewActions by lazy {
        MutableSharedFlow<Action?>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_LATEST
        )
    }

    fun viewStates(): WrappedStateFlow<State> = WrappedStateFlow(_viewStates.asStateFlow())
    fun viewActions(): WrappedSharedFlow<Action?> = WrappedSharedFlow(_viewActions.asSharedFlow())

    protected var viewState: State
        get() = _viewStates.value
        set(value) {
            _viewStates.value = value
        }

    protected var viewAction: Action?
        get() = _viewActions.replayCache.last()
        set(value) {
            println("Try emit action result: " + _viewActions.tryEmit(value))
        }

    protected fun clearActions() {
        viewAction = null
    }

    abstract fun obtainEvent(viewEvent: Event)
}
