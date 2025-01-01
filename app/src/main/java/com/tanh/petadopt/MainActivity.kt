package com.tanh.petadopt

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.identity.Identity
import com.tanh.petadopt.data.GoogleAuthUiClient
import com.tanh.petadopt.presentation.EntireScreen
import com.tanh.petadopt.presentation.add.AddScreen
import com.tanh.petadopt.presentation.add.AddViewModel
import com.tanh.petadopt.presentation.authentication.Login
import com.tanh.petadopt.presentation.authentication.LoginUiState
import com.tanh.petadopt.presentation.authentication.LoginViewModel
import com.tanh.petadopt.presentation.favorites.FavoriteScreen
import com.tanh.petadopt.presentation.home.Home
import com.tanh.petadopt.presentation.home.HomeViewModel
import com.tanh.petadopt.presentation.pet_detail.DetailScreen
import com.tanh.petadopt.presentation.pet_detail.DetailViewModel
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
                val loginViewModel = hiltViewModel<LoginViewModel>()
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val detailViewModel = hiltViewModel<DetailViewModel>()
                val addViewModel = hiltViewModel<AddViewModel>()
                val navController = rememberNavController()

                val state = loginViewModel.state.collectAsState(initial = LoginUiState()).value

                var isLoggedIn by remember {
                    mutableStateOf(state.isLoginSuccessful)
                }

                Scaffold(
                    bottomBar = {
                        if (isLoggedIn) {
                            EntireScreen(navController = navController)
                        }
                    }
                ) { paddings ->
                    NavHost(
                        modifier = Modifier.padding(paddings),
                        navController = navController,
                        startDestination = Util.LOG_IN
                    ) {
                        composable(Util.LOG_IN) {
                            isLoggedIn = false
                            Login(
                                viewModel = loginViewModel
                            ) {
                                navController.navigate(it.route)
                            }
                        }
                        composable(Util.HOME) {
                            isLoggedIn = true
                            Home(viewModel = homeViewModel) {
                                navController.navigate(it.route)
                            }
                        }

                        composable(
                            route = Util.FAVORITE
                        ) {
                            isLoggedIn = true
                            FavoriteScreen(viewModel = homeViewModel)
                        }

                        composable(
                            route = Util.DETAIL + "/{petId}",
                            arguments = listOf(
                                navArgument(
                                    name = "petId"
                                ) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) {
                            isLoggedIn = false
                            val petId = it.arguments?.getString("petId") ?: ""
                            DetailScreen(
                                viewModel = detailViewModel,
                                petId = petId
                            ) { event ->
                                navController.navigate(event.route)
                            }
                        }

                        composable(Util.ADD) {
                            isLoggedIn = false
                            AddScreen(
                                viewModel = addViewModel
                            ) {
                                navController.navigate(it.route)
                            }
                        }
                    }
                }
            }
        }
    }
}

