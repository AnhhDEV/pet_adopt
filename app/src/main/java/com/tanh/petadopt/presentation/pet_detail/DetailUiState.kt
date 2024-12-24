package com.tanh.petadopt.presentation.pet_detail

import com.tanh.petadopt.domain.dto.PetDto

data class DetailUiState(
    val isLoading: Boolean? = false,
    val pet: PetDto? = null,
    val error: String = ""
)