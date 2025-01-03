package com.tanh.petadopt.presentation.inbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanh.petadopt.converter.TimeDefinition
import com.tanh.petadopt.data.ChatRepository
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.domain.model.onError
import com.tanh.petadopt.domain.model.onSuccess
import com.tanh.petadopt.presentation.OneTimeEvent
import com.tanh.petadopt.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val auth: GoogleAuthUiClient,
    private val chatRepository: ChatRepository
): ViewModel() {

    private val _state = MutableStateFlow(InboxUiState())
    val state = _state.asStateFlow()

    private val _channel = Channel<OneTimeEvent>()
    val channel = _channel.receiveAsFlow()

    suspend fun getChats() {
        _state.value = _state.value.copy(
            isLoading = true
        )
        chatRepository.getChats(
            userId = auth.getSignedInUser()?.userId ?: ""
        ).collect { res ->
            res.onSuccess  {
                Log.d("chat", it.joinToString { chat -> chat.chatId.toString() })
               _state.value = _state.value.copy(
                   isLoading = false,
                   chats = it
               )
            }
            res.onError {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    fun onNavToDetailChatting(chatId: String) {
        sendEvent(OneTimeEvent.Navigate(Util.MESSENGER + "/$chatId"))
    }

    private fun sendEvent(event: OneTimeEvent) {
        viewModelScope.launch {
            _channel.send(event)
        }
    }

}