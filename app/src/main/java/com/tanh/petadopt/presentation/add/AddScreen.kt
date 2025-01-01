package com.tanh.petadopt.presentation.add

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tanh.petadopt.presentation.OneTimeEvent
import com.tanh.petadopt.ui.theme.Gray
import com.tanh.petadopt.ui.theme.PetAdoptTheme
import com.tanh.petadopt.ui.theme.Yellow100
import com.tanh.petadopt.ui.theme.Yellow60
import com.tanh.petadopt.util.Util

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    viewModel: AddViewModel? = null,
    onNavigate: (OneTimeEvent.Navigate) -> Unit
) {

    val state = viewModel?.state?.collectAsState(initial = AddUiState())?.value ?: AddUiState()

    val context = LocalContext.current

    val isNameError = state.nameError == null

    var inputName by remember {
        mutableStateOf("")
    }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel?.onUriPhotoChange(uri.toString())
            }
        }


    LaunchedEffect(true) {
        viewModel?.chanel?.collect { event ->
            when (event) {
                is OneTimeEvent.Navigate -> {
                    onNavigate(event)
                }

                is OneTimeEvent.ShowSnackbar -> TODO()
                is OneTimeEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel?.onNavToHome()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Add New Pet",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f)
            )
        }

        //photo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            //Chọn ảnh
            Text(
                text = "Add New Pet for adaption",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            AsyncImage(
                model = state.uri.ifBlank { Util.CAT_PAW_URL },
                contentDescription = "Add new pet",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = Gray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
            )

            //Chọn tên
            Text(
                text = if (isNameError) "Pet Name *"
                else "Pet Name *  ${state.nameError}",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = if (isNameError) Color.Black else Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            TextField(
                value = inputName,
                onValueChange = { it ->
                    inputName = it
                    viewModel?.onNameChange(it)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (isNameError) Color.White else Color.Red
                    )
                    .onFocusChanged { focusState ->
                        if(focusState.isFocused && !isNameError) {
                            viewModel?.resetNameState()
                        }
                    },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.White
                )
            )


            //Chọn breed
            Text(
                text = if (isNameError) "Breed *"
                else "Breed *  ${state.nameError}",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = if (isNameError) Color.Black else Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            TextField(
                value = inputName,
                onValueChange = { it ->
                    inputName = it
                    viewModel?.onBreedChange(it)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (isNameError) Color.White else Color.Red
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.White
                )
            )

            //button
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    viewModel?.onInsertPet()
                },
                colors = ButtonColors(
                    containerColor = Yellow60,
                    contentColor = Color.Black,
                    disabledContainerColor = Yellow60,
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Submit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddScreen(modifier: Modifier = Modifier) {
    PetAdoptTheme {
        AddScreen { }
    }
}