package com.tanh.petadopt.presentation.pet_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.data.PetRepository
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
class DetailViewModel @Inject constructor (
    private val auth: GoogleAuthUiClient,
    private val petRepository: PetRepository
): ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state = _state.asStateFlow()

    private val _channel = Channel<OneTimeEvent> {  }
    val channel = _channel.receiveAsFlow()

    fun getPetByAnimalId(petId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            petRepository.getPetById(
                petId = petId,
                userId = auth.getSignedInUser()?.userId ?: ""
            ).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    pet = it
                )
            }.onError {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = it.message ?: "You get exception!"
                )
            }
        }
    }

    fun navToHome() {
        sendEvent(OneTimeEvent.Navigate(Util.HOME))
    }

    private fun sendEvent(event: OneTimeEvent) {
        viewModelScope.launch {
            _channel.send(event)
        }
    }

}