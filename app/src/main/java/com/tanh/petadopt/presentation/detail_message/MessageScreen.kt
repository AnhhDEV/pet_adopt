package com.tanh.petadopt.presentation.detail_message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tanh.petadopt.domain.model.Message
import com.tanh.petadopt.presentation.OneTimeEvent
import com.tanh.petadopt.presentation.components.MessageItem
import com.tanh.petadopt.ui.theme.PetAdoptTheme

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    viewModel: MessageViewModel? = null,
    chatId: String,
    receiverId: String,
    onNavigate: (OneTimeEvent.Navigate) -> Unit
) {

    val state =
        viewModel?.state?.collectAsState(initial = MessageUiState())?.value ?: MessageUiState()

    var inputMessage by remember {
        mutableStateOf("")
    }

    LaunchedEffect(true) {
        viewModel?.channel?.collect { event ->
            when (event) {
                is OneTimeEvent.Navigate -> {
                    onNavigate(event)
                }

                is OneTimeEvent.ShowSnackbar -> TODO()
                is OneTimeEvent.ShowToast -> TODO()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel?.getMessages(chatId = chatId)
        viewModel?.getUserById(id = receiverId)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            AsyncImage(
                model = state.receiver?.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = state.receiver?.name ?: "Anonymous",
                fontSize = 20.sp
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = inputMessage,
                        onValueChange = {
                            inputMessage = it
                            viewModel?.onMessageChange(it)
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(text = "Type your message")
                        }
                    )
                    IconButton(
                        onClick = {
                            viewModel?.createMessage(
                                chatId = chatId
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null
                        )
                    }
                }
            }

            items(state.messages) { message ->
                MessageItem(
                    message = message,
                    userId = viewModel?.getUserId ?: ""
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewMessageScreen(modifier: Modifier = Modifier) {
    PetAdoptTheme {
        MessageScreen(modifier = modifier, chatId = "", receiverId = "") {

        }
    }
}