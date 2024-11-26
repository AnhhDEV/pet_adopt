package com.tanh.petadopt.presentation.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tanh.petadopt.domain.model.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()



    fun onSignInResult(result: SignInResult) {
        Log.d("login", "run4")
        _state.update {
            it.copy(
                isLoginSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
        Log.d("login", "run5")
    }


    fun resetState() {
        _state.update {
            LoginUiState()
        }
    }

}