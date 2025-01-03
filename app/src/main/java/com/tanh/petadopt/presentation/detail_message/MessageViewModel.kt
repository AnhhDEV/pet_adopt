package com.tanh.petadopt.presentation.detail_message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.tanh.petadopt.data.ChatRepository
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.domain.dto.UserDto
import com.tanh.petadopt.domain.model.Message
import com.tanh.petadopt.domain.model.onError
import com.tanh.petadopt.domain.model.onSuccess
import com.tanh.petadopt.presentation.OneTimeEvent
import com.tanh.petadopt.presentation.inbox.InboxUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val auth: GoogleAuthUiClient,
    private val repository: ChatRepository
): ViewModel() {

    private val _state = MutableStateFlow(MessageUiState())
    val state = _state.asStateFlow()

    private val _channel = Channel<OneTimeEvent>()
    val channel = _channel.receiveAsFlow()

    val getUserId: String
        get() = auth.getSignedInUser()?.userId ?: ""

    suspend fun getMessages(chatId: String) {
        _state.value = _state.value.copy(
            isLoading = true
        )
        repository.getMessages(chatId = chatId).collect { message ->
            message.onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    messages = it
                )
            }
            message.onError {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    fun getUserById(id: String) {
        viewModelScope.launch {
             _state.value = _state.value.copy(
                 receiver = repository.getUser(id = id)
             )
        }
    }

    fun createMessage(chatId: String) {
        viewModelScope.launch {
            repository.createMessage(chatId = chatId, message = Message(
                content = _state.value.onMessageChange,
                uid = getUserId,
                time = Timestamp.now()
            ))
        }
    }

    fun onMessageChange(message: String) {
        _state.value = _state.value.copy(
            onMessageChange = message
        )
    }

    private fun sendEvent(event: OneTimeEvent) {
        viewModelScope.launch {
            _channel.send(event)
        }
    }

}