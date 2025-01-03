package com.tanh.petadopt.presentation.detail_message

import com.tanh.petadopt.domain.dto.UserDto
import com.tanh.petadopt.domain.model.Message

data class MessageUiState(
    val isLoading: Boolean? = false,
    val error: String? = "",
    val messages: List<Message> = emptyList(),
    val receiver: UserDto? = null,
    val onMessageChange: String = "",
)