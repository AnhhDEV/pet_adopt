package com.tanh.petadopt.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.data.PetRepository
import com.tanh.petadopt.presentation.OneTimeEvent
import com.tanh.petadopt.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val auth: GoogleAuthUiClient,
    private val petRepository: PetRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddUiState())
    val state = _state.asStateFlow()

    private val _channel = Channel<OneTimeEvent>()
    val chanel = _channel.receiveAsFlow()

    fun onInsertPet() {
        if(isValidInput()) {
            val name = _state.value.name
            val photoUrl = _state.value.uri
            val category = _state.value.category
            val age = convertToDouble(_state.value.age, "age") ?: 0.0
            val weight = convertToDouble(_state.value.weight, "weight") ?: 0.0
            val breed = _state.value.breed
            val gender = _state.value.gender
            val address = _state.value.address
            val about = _state.value.about
            val ownerId = auth.getSignedInUser()?.userId ?: ""

            insertPet(
                ownerId = ownerId,
                name = name,
                age = age,
                weight = weight,
                breed = breed,
                category = category,
                gender = gender,
                photoUrl = photoUrl,
                address = address,
                about = about
            )

            resetState()
            sendEvent(OneTimeEvent.Navigate(Util.HOME))
        } else {
            sendEvent(OneTimeEvent.ShowToast("Please fill in all fields"))
        }
    }

    private fun insertPet(
        ownerId: String,
        name: String,
        age: Double,
        weight: Double,
        breed: String,
        category: String,
        gender: Boolean,
        photoUrl: String,
        address: String,
        about: String
    ) {
        viewModelScope.launch {
            petRepository.insertPet(
                ownerId = ownerId,
                name = name,
                age = age,
                weight = weight,
                breed = breed,
                category = category,
                gender = gender,
                photoUrl = photoUrl,
                address = address,
                about = about
            )
        }
    }

    private fun isValidInput(): Boolean {
        if(_state.value.uri.isBlank()) {
            _state.value = _state.value.copy(photoUrl = "Photo cannot be empty")
            return false
        }
        if(_state.value.name.isBlank()) {
            _state.value = _state.value.copy(nameError = "Name cannot be empty")
            return false
        }
        if(_state.value.age.isBlank()) {
            _state.value = _state.value.copy(ageError = "Age cannot be empty")
            return false
        }
        if (_state.value.breed.isBlank()) {
            _state.value = _state.value.copy(breedError = "Breed cannot be empty")
            return false
        }
        if (_state.value.weight.isBlank()) {
            _state.value = _state.value.copy(weightError = "Weight cannot be empty")
            return false
        }
        if (_state.value.address.isBlank()) {
            _state.value = _state.value.copy(addressError = "Address cannot be empty")
            return false
        }
        if (_state.value.about.isBlank()) {
            _state.value = _state.value.copy(aboutError = "About cannot be empty")
        }
        return true
    }

    private fun convertToDouble(number: String, field: String): Double? {
        if(number.any { it.isLetter() }) {
            if(field == "age") {
                _state.value = _state.value.copy(ageError = "Don't type string in this field")
            } else if(field == "weight") {
                _state.value = _state.value.copy(weightError = "Don't type string in this field")
            }
            return null
        }
        val newNumber = number.replace(",", ".")
        return try {
            newNumber.toDouble()
        } catch (e: NumberFormatException) {
            if(field == "age") {
                _state.value = _state.value.copy(ageError = "Invalid number")
            } else if(field == "weight") {
                _state.value = _state.value.copy(weightError = "Invalid number")
            }
            null
        }
    }

    private fun resetState() {
        _state.value = AddUiState()
    }

    fun resetNameState() {
        _state.value = _state.value.copy(
            nameError = null
        )
    }

    fun onNavToHome() {
        resetState()
        sendEvent(OneTimeEvent.Navigate(Util.HOME))
    }

    fun onUriPhotoChange(uri: String) {
        _state.value = _state.value.copy(uri = uri)
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun onCategorySelect(name: String) {
        _state.value = _state.value.copy(category = name)
    }

    fun onBreedChange(breed: String) {
        _state.value = _state.value.copy(breed = breed)
    }

    fun onAgeChange(age: String) {
        _state.value = _state.value.copy(age = age)
    }

    fun onWeightChange(weight: String) {
        _state.value = _state.value.copy(weight = weight)
    }

    fun onGenderChange(gender: Boolean) {
        _state.value = _state.value.copy(gender = gender)
    }

    fun onAddressChange(address: String) {
        _state.value = _state.value.copy(address = address)
    }

    fun onAboutChange(about: String) {
        _state.value = _state.value.copy(about = about)
    }

    private fun sendEvent(event: OneTimeEvent) {
        _channel.trySend(event)
    }

}