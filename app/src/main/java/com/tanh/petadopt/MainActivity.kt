package com.tanh.petadopt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.presentation.EntireScreen
import com.tanh.petadopt.presentation.authentication.Login
import com.tanh.petadopt.presentation.authentication.LoginViewModel
import com.tanh.petadopt.presentation.home.Home
import com.tanh.petadopt.presentation.home.HomeViewModel
import com.tanh.petadopt.ui.theme.Gray
import com.tanh.petadopt.ui.theme.PetAdoptTheme
import com.tanh.petadopt.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetAdoptTheme {

                var isLoggedIn by remember {
                    mutableStateOf(false)
                }

                val loginViewModel = hiltViewModel<LoginViewModel>()
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        if(isLoggedIn) {
                            EntireScreen(navController = navController)
                        }
                    }
                ) {paddings ->
                    NavHost(
                        modifier = Modifier.padding(paddings),
                        navController = navController,
                        startDestination = Util.LOG_IN
                    ) {
                        composable(Util.LOG_IN) {
                            Login(
                                viewModel = loginViewModel
                            ) {
                                navController.navigate(it.route)
                                isLoggedIn = !isLoggedIn
                            }
                        }
                        composable(Util.HOME) {
                            Home(viewModel = homeViewModel) {
                                isLoggedIn = !isLoggedIn
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
}

