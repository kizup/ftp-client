package ru.kizapp.ftpclient.ui.screens.connection.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddConnectionScreen(
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        val textFieldModifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        val connectionNameValue = remember { mutableStateOf("") }
        TextField(
            value = connectionNameValue.value,
            modifier = textFieldModifier,
            placeholder = {
                Text(text = "Connection name")
            },
            onValueChange = {},
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next,
            )
        )

        val connectionHostValue = remember { mutableStateOf("") }
        TextField(
            value = connectionHostValue.value,
            modifier = textFieldModifier,
            placeholder = {
                Text(text = "Host")
            },
            onValueChange = {},
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next,
            )
        )
        val connectionPortValue = remember { mutableStateOf("") }
        TextField(
            value = connectionPortValue.value,
            onValueChange = {},
            modifier = textFieldModifier,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            placeholder = {
                Text("Port (by default)")
            }
        )

        val connectionUserNameValue = remember { mutableStateOf("") }
        TextField(
            value = connectionUserNameValue.value,
            onValueChange = {},
            placeholder = {
                Text("Username")
            },
            modifier = textFieldModifier,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next,
            )
        )

        val connectionPasswordValue = remember { mutableStateOf("") }
        TextField(
            value = connectionPasswordValue.value,
            onValueChange = {},
            placeholder = {
                Text("Password")
            },
            modifier = textFieldModifier,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
        )

        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        Button(
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text("Add")
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun AddConnectionScreenPreview() {
    AddConnectionScreen(rememberNavController())
}
