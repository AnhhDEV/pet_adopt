package com.tanh.petadopt.domain.model

import com.google.firebase.Timestamp

data class Chat(
    val chatId: String? = "",
    val fromId: String? = "",
    val fromName: String? = "",
    val fromAvatar: String? = "",
    val toId: String? = "",
    val toName: String? = "",
    val toAvatar: String? = "",
    val lastMessage: String? = "",
    val lastTime: Timestamp? = null
)
