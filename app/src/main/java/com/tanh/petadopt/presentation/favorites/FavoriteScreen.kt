package com.tanh.petadopt.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tanh.petadopt.presentation.components.PetItem
import com.tanh.petadopt.presentation.home.HomeUIState
import com.tanh.petadopt.presentation.home.HomeViewModel

@Composable
fun FavoriteScreen (
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel? = null
) {

    val state = viewModel?.state?.collectAsState(initial = HomeUIState())?.value ?: HomeUIState()

    val haveFavorite by remember {
        mutableStateOf(state.pets.isNotEmpty())
    }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Favorite",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(state.pets) { pet ->
                if(pet.isFavorite == true) {
                    PetItem(
                        pet = pet,
                        onAddFavorite = {
                            if(it.second) {
                                viewModel?.addFavorite(it.first)
                            } else {
                                viewModel?.removeFavorite(it.first)
                            }
                        },
                    ) {

                    }
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewFavoriteScreen() {
    FavoriteScreen()
}