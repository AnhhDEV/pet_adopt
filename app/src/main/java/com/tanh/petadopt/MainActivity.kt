package com.tanh.petadopt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.presentation.authentication.Login
import com.tanh.petadopt.presentation.authentication.LoginViewModel
import com.tanh.petadopt.ui.theme.PetAdoptTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetAdoptTheme {
                val loginViewModel = viewModel<LoginViewModel>()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "signin"
                ) {
                    composable("signin") {
                        Login (
                           googleAuthUiClient = googleAuthUiClient,
                            navController = navController,
                            viewModel = loginViewModel
                        )
                    }
                }
            }
        }
    }
}

