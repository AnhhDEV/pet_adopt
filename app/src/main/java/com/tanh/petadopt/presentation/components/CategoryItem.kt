package com.tanh.petadopt.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tanh.petadopt.R
import com.tanh.petadopt.ui.theme.Blue
import com.tanh.petadopt.ui.theme.Yellow00
import com.tanh.petadopt.ui.theme.Yellow40

@Composable
fun CategoryItem (
    modifier: Modifier = Modifier,
    category: Pair<String, Int>,
    onChangeClicked: (Boolean) -> Unit,
    onCLick: (String) -> Unit
) {

    var isClicked by remember {
        mutableStateOf(false)
    }

    val backgroundColor = if(!isClicked) {
        Yellow00
    } else {
        Blue
    }
    val strokeColor = if(!isClicked) {
        Yellow40
    } else {
        Blue
    }
    Column(
        modifier = modifier.
            clickable {
                isClicked = !isClicked
                onCLick(category.first)
                onChangeClicked(isClicked)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, strokeColor, shape = RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .width(80.dp)
                .height(70.dp)
        ) {
            Image(
                painter = painterResource(id = category.second),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .aspectRatio(1f)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.first,
            fontSize = 16.sp
        )
    }
}
