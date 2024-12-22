package com.tanh.petadopt.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.tanh.petadopt.domain.dto.PetDto
import com.tanh.petadopt.domain.model.Pet
import com.tanh.petadopt.ui.theme.Yellow00
import com.tanh.petadopt.ui.theme.Yellow60

@Composable
fun PetItem(
    modifier: Modifier = Modifier,
    pet: PetDto,
    onAddFavorite: (Pair<String, Boolean>) -> Unit,
    onDetail: (String) -> Unit
) {
    // Theo dõi thay đổi `pet` và cập nhật trạng thái `isFavorite`
    var isFavorite by remember(pet.animalId) {
        mutableStateOf(pet.isFavorite ?: false)
    }

    Log.d("test", "${pet.name}: $isFavorite")

    val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    val color = if (isFavorite) Color.Red else Color.White

    Box(
        modifier = modifier
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
            .width(180.dp)
            .height(200.dp)
            .clickable {
                onDetail(pet.animalId ?: "")
            }
    ) {
        Box(
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            AsyncImage(
                model = pet.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .width(150.dp)
                    .height(150.dp)
                    .aspectRatio(1f)
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        isFavorite = !isFavorite
                        onAddFavorite(Pair(pet.animalId ?: "", isFavorite))
                    }
            )
        }
        Box(
            modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 8.dp)
        ) {
            Column {
                Text(
                    text = pet.name ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Gelman",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
        Surface(
            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Yellow00)
            ) {
                Text(
                    text = "   " + pet.age.toString() + " YRS   ",
                    color = Yellow60,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
