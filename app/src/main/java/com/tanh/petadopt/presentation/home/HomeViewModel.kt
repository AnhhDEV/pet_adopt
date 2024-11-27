package com.tanh.petadopt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.data.PetRepository
import com.tanh.petadopt.data.UserRepository
import com.tanh.petadopt.domain.model.UserData
import com.tanh.petadopt.domain.model.onError
import com.tanh.petadopt.domain.model.onSuccess
import com.tanh.petadopt.presentation.OneTimeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val googleAuth: GoogleAuthUiClient,
    private val repository: PetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUIState())
    val state = _state.asStateFlow()

    private val _channel = Channel<OneTimeEvent>()
    val channel = _channel.receiveAsFlow()

    fun getUser() {
        val user = googleAuth.getSignedInUser()
        _state.update {
            it.copy(
                userData = user
            )
        }
    }

    fun onFiltered() {
        _state.update {
            it.copy(
                isFiltered = !it.isFiltered
            )
        }
    }

    suspend fun getAllPetsByCategory(category: String) {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        repository.getAllPetsByCategory(category = category).collect { result ->
            result.run {
                onSuccess {list ->
                    _state.update {
                        it.copy(
                            pets = list,
                            isLoading = false
                        )
                    }
                }
                onError {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = it.errorMessage
                        )
                    }
                }
            }
        }
    }

    suspend fun getAllPets() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        repository.getAllPets().collect { result ->
            result.run {
                onSuccess {list ->
                    _state.update {
                        it.copy(
                            pets = list,
                            isLoading = false
                        )
                    }
                }
                onError {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = it.errorMessage
                        )
                    }
                }
            }
        }
    }


    private fun sendEvent(event: OneTimeEvent) {
        viewModelScope.launch {
            _channel.send(event)
        }
    }

}