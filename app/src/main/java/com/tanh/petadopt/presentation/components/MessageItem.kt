package com.tanh.petadopt.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tanh.petadopt.domain.model.Message

@Composable
fun MessageItem (
    modifier: Modifier = Modifier,
    message: Message,
    userId: String
) {

    val widthScreen = LocalConfiguration.current.screenWidthDp.dp

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        if(message.uid == userId) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = message.content ?: "No content",
                fontSize = 16.sp,
                modifier = Modifier.widthIn(max = widthScreen / 2),
                textAlign = TextAlign.Start
            )
        } else {
            Text(
                text = message.content ?: "No content",
                fontSize = 16.sp,
                modifier = Modifier.widthIn(max = widthScreen / 2),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }

}