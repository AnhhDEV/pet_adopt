package com.tanh.petadopt.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.tanh.petadopt.domain.model.UserData
import com.tanh.petadopt.presentation.authentication.LoginViewModel

@Composable
fun Home (
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel? = null,
    onPopBack: () -> Unit
) {
    val user = viewModel?.getCurrentUser()

    Column {
        if(user != null) {
            Text(text = "Hello ${user.username}")
            Text(text = "Your email is ${user.userId}")
            AsyncImage(
                model = user.profilePictureUrl,
                contentDescription = null
            )
            Button(onClick = {
                viewModel.onLogout()
                onPopBack()
            }) { }
        }
    }

}