package com.tanh.petadopt.presentation

sealed class OneTimeEvent {
    data class Navigate(val route: String): OneTimeEvent()
    data class ShowSnackbar(val message: String): OneTimeEvent()
}