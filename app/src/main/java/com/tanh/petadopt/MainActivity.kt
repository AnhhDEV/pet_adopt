package com.tanh.petadopt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.presentation.authentication.Login
import com.tanh.petadopt.presentation.authentication.LoginViewModel
import com.tanh.petadopt.presentation.home.Home
import com.tanh.petadopt.ui.theme.PetAdoptTheme
import com.tanh.petadopt.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetAdoptTheme {
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Util.LOG_IN
                ) {
                    composable(Util.LOG_IN) {
                        Login(
                            viewModel = loginViewModel
                        ) {
                            navController.navigate(it.route)
                        }
                    }
                    composable(Util.HOME) {
                        Home(viewModel = loginViewModel) {
                            navController.navigate(Util.LOG_IN) {
                                launchSingleTop = true
                                popUpTo(route = Util.LOG_IN) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

