package ru.kizapp.ftpclient.ui.screens.connection.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.kizapp.ftpclient.navigation.NavigationTree
import ru.kizapp.ftpclient.ui.screens.connection.add.models.AddConnectionAction
import ru.kizapp.ftpclient.ui.screens.connection.add.models.AddConnectionEvent
import ru.kizapp.ftpclient.ui.screens.connection.add.models.AddConnectionState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)
@Composable
fun AddConnectionScreen(
    navController: NavController,
    viewModel: AddConnectionViewModel,
) {
    val viewAction by viewModel.viewActions().collectAsState(initial = null)
    val viewState by viewModel.viewStates().collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        InputFields(viewModel = viewModel, viewState = viewState)

        if (viewState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        }
    }

    LaunchedEffect(key1 = viewAction) {
        viewModel.obtainEvent(AddConnectionEvent.ClearAction)
        when (viewAction) {
            AddConnectionAction.HideKeyboard -> {
                keyboardController?.hide()
                focusManager.clearFocus()
            }

            AddConnectionAction.ShowConnectionsList -> {
                navController.navigate(NavigationTree.Root.ConnectionList.name) {
                    popUpTo(NavigationTree.Root.AddConnection.name) { inclusive = true }
                }
            }

            null -> Unit
        }
    }
}

@Composable
private fun InputFields(
    viewModel: AddConnectionViewModel,
    viewState: AddConnectionState,
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
        AddConnectionTextField(
            modifier = textFieldModifier,
            value = viewState.connectionName.orEmpty(),
            placeholder = "Connection name",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onValueChanged = { viewModel.obtainEvent(AddConnectionEvent.OnConnectionNameChanged(it)) }
        )
        AddConnectionTextField(
            modifier = textFieldModifier,
            value = viewState.connectionHost,
            placeholder = "Host",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onValueChanged = { viewModel.obtainEvent(AddConnectionEvent.OnHostChanged(it)) }
        )
        AddConnectionTextField(
            modifier = textFieldModifier,
            value = viewState.connectionPort.toString(),
            placeholder = "Port (by default)",
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            onValueChanged = { viewModel.obtainEvent(AddConnectionEvent.OnPortChanged(it)) }
        )
        AddConnectionTextField(
            modifier = textFieldModifier,
            value = viewState.userName.orEmpty(),
            placeholder = "Username",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onValueChanged = { viewModel.obtainEvent(AddConnectionEvent.OnUserNameChanged(it)) }
        )
        AddConnectionTextField(
            modifier = textFieldModifier,
            value = viewState.password.orEmpty(),
            placeholder = "Password",
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onValueChanged = { viewModel.obtainEvent(AddConnectionEvent.OnPasswordChanged(it)) }
        )
        Button(
            onClick = { viewModel.obtainEvent(AddConnectionEvent.OnAddClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text("Add")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddConnectionTextField(
    value: String,
    modifier: Modifier = Modifier,
    placeholder: String,
    autoCorrect: Boolean = false,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next,
    onValueChanged: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = { Text(placeholder) },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = autoCorrect,
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = capitalization,
        ),
    )
}
