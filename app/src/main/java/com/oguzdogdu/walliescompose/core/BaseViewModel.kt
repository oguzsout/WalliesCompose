package com.oguzdogdu.walliescompose.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : ViewState, Event : ViewEvent, Effect : ViewEffect>(
    initialState: State,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow(replay = 1, extraBufferCapacity = 4)
    val effect = _effect.asSharedFlow()

    private val _eventChannel: Channel<Event> = Channel(Channel.BUFFERED)
    val eventChannel = _eventChannel.receiveAsFlow()

    val currentState: State
        get() = _state.value

    fun <T> sendApiCall(
        request: suspend () -> Flow<T>,
        onLoading: (Boolean) -> Unit,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ): Job {
        return viewModelScope.launch {
            try {
                request()
                    .onStart {
                        onLoading(true)
                    }
                    .catch { exception ->
                        onLoading(false)
                        onError(exception)
                    }
                    .onCompletion { cause ->
                        onLoading(false)
                        cause?.let { onError(it) }
                        onComplete()
                    }
                    .collectLatest { result ->
                        onLoading(false)
                        onSuccess(result)
                    }
            } catch (exception: Throwable) {
                onLoading(false)
                onError(exception)
            }
        }
    }

    fun setState(state: State) {
        viewModelScope.launch {
            _state.update { state }
        }
    }

    fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _eventChannel
                .send(event)
                .run {
                    eventChannel.collect {
                        handleEvents(it)
                    }
                }
        }
    }

     open fun handleEvents(event: Event) {}
}
