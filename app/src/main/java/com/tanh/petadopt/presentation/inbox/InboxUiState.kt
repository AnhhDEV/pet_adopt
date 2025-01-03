package com.tanh.petadopt.presentation.inbox

import com.tanh.petadopt.domain.model.Chat

data class InboxUiState(
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
