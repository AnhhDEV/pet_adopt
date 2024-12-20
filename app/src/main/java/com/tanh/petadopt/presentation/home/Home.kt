package com.tanh.petadopt.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tanh.petadopt.R
import com.tanh.petadopt.presentation.OneTimeEvent
import com.tanh.petadopt.presentation.components.CategoryItem
import com.tanh.petadopt.presentation.components.PetItem
import com.tanh.petadopt.ui.theme.Yellow60
import com.tanh.petadopt.util.Util
import kotlinx.coroutines.launch

@Composable
fun Home (
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel? = null,
    onNav: (OneTimeEvent.Navigate) -> Unit
) {

    val state = viewModel?.state?.collectAsState(initial = HomeUIState())?.value ?: HomeUIState()
    val channel = viewModel?.channel

    val context = LocalContext.current
    val isError = state.errorMessage != null

    val scope = rememberCoroutineScope()

    var category by remember {
        mutableStateOf("")
    }
    var isClicked by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(category) {
        if(category.isNotEmpty()) {
            viewModel?.getAllPetsByCategory(category)
        }
    }

    LaunchedEffect(isClicked) {
        if(!isClicked) {
            viewModel?.getAllPets()
        }
    }

    LaunchedEffect(Unit) {
        viewModel?.getUser()
        viewModel?.getAllPets()
    }

    LaunchedEffect(true) {
        channel?.collect { event ->
            when(event) {
                is OneTimeEvent.Navigate -> {
                    onNav(event)
                }
                is OneTimeEvent.ShowSnackbar -> TODO()
                is OneTimeEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome,",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = state.userData?.username ?: "Anonymous",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            AsyncImage(
                model = state.userData?.profilePictureUrl,
                contentDescription = null,
                modifier = Modifier.height(50.dp)
                    .padding(start = 130.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.panel1),
                    contentDescription = null,
                    modifier = Modifier
                        .width(362.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
            item {
                Image(
                    painter = painterResource(id = R.drawable.panel2),
                    contentDescription = null,
                    modifier = Modifier
                        .width(350.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Category",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(Util.categories) {(pair, click) ->
                CategoryItem(
                    category = pair,
                    onChangeClicked = {
                        isClicked = it
                    }
                ) {
                    category = it
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(state.pets) { pet ->
                PetItem(
                    modifier = Modifier.clickable {
                        viewModel?.onNavToDetail(pet.animalId ?: "Unknown id")
                    },
                    pet = pet,
                    onAddFavorite = {

                    }
                ) { }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 2.dp,
                    color = Yellow60,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {

                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pets),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                        .aspectRatio(1f),
                    tint = Yellow60
                )
                Text(
                    text = "Add New Pet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewHome() {
    Home() {

    }
}