package com.tanh.petadopt.presentation.authentication

import android.app.Activity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.domain.model.SignInResult
import com.tanh.petadopt.domain.model.UserData
import com.tanh.petadopt.presentation.OneTimeEvent
import com.tanh.petadopt.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleAuth: GoogleAuthUiClient
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _channel = Channel<OneTimeEvent>()
    val channel = _channel.receiveAsFlow()

    fun onNavToHome() {
        sendEvent(OneTimeEvent.Navigate(route = Util.HOME))
    }

    fun getCurrentUser(): UserData? {
        return googleAuth.getSignedInUser()
    }

    fun loginSuccessfully() {
        if(_state.value.isLoginSuccessful == true) {
            sendEvent(OneTimeEvent.ShowSnackbar(message = "Login successfully"))
            sendEvent(OneTimeEvent.Navigate(route = Util.HOME))
            resetState()
        }
    }

    fun onGetIntent(result: ActivityResult) {
        viewModelScope.launch {
           if(result.resultCode == Activity.RESULT_OK) {
               val signInResult = googleAuth.signInWithIntent(
                   intent = result.data ?: return@launch
               )
               onSignInResult(result = signInResult)
           }
        }
    }

    fun onLogin(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) {
        viewModelScope.launch {
            val loginResult = googleAuth.signIn()
            launcher.launch(
                IntentSenderRequest.Builder(
                    loginResult ?: return@launch
                ).build()
            )
        }
    }

    private fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isLoginSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }


    private fun resetState() {
        _state.update {
            LoginUiState()
        }
    }

    private fun sendEvent(event: OneTimeEvent) {
        viewModelScope.launch {
            _channel.send(event)
        }
    }

}