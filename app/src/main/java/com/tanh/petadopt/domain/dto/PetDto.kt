package com.tanh.petadopt.domain.dto

data class PetDto(
    val animalId: String? = "",
    val ownerId: String? = "",
    val name: String? = "",
    val age: Long? = 0,
    val weight: Long? = 0,
    val breed: String? = "",
    val category: String? = "",
    val gender: Boolean? = false,
    val photoUrl: String? = "",
    val address: String? = "",
    val about: String? = "",
    var isFavorite: Boolean? = false
)