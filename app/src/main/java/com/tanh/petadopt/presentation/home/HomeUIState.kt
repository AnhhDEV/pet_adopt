package com.tanh.petadopt.presentation.home

import com.tanh.petadopt.domain.model.Pet
import com.tanh.petadopt.domain.model.UserData

data class HomeUIState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: UserData? = null
)
