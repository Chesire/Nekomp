package com.chesire.nekomp.feature.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.viewEvent) {
        when (state.viewEvent) {
            is ViewEvent.LoginFailure -> {
                // Log success
            }

            ViewEvent.LoginSuccessful -> {
                // Log failure
            }

            null -> Unit
        }

        viewModel.execute(ViewAction.ObservedViewEvent)
    }

    Render(
        state = state,
        execute = { viewModel.execute(it) }
    )
}

@Composable
private fun Render(
    state: UIState,
    execute: (ViewAction) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            ElevatedCard(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UsernameInput(
                        username = state.email,
                        onUsernameChanged = { execute(ViewAction.EmailUpdated(it)) }
                    )
                    PasswordInput(
                        password = state.password,
                        isLoggingIn = state.isPendingLogin,
                        onPasswordChanged = { execute(ViewAction.PasswordUpdated(it)) },
                        onLoginPressed = { execute(ViewAction.LoginPressed) }
                    )
                    if (state.isPendingLogin) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                    } else {
                        LoginButton(
                            isLoggingIn = state.isPendingLogin,
                            modifier = Modifier.padding(top = 8.dp),
                            onLoginPressed = { execute(ViewAction.LoginPressed) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UsernameInput(
    username: String,
    onUsernameChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChanged,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        label = {
            //Text(text = stringResource(id = StringResource.login_username))
            "Username"
        }
    )
}

@Composable
private fun PasswordInput(
    password: String,
    isLoggingIn: Boolean,
    onPasswordChanged: (String) -> Unit,
    onLoginPressed: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null // TODO
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (!isLoggingIn) {
                    onLoginPressed()
                    keyboardController?.hide()
                }
            }
        ),
        singleLine = true,
        label = {
            // Text(text = stringResource(id = StringResource.login_password))
            "Password"
        }
    )
}

@Composable
private fun LoginButton(
    isLoggingIn: Boolean,
    modifier: Modifier = Modifier,
    onLoginPressed: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        modifier = modifier,
        onClick = {
            if (!isLoggingIn) {
                onLoginPressed()
                keyboardController?.hide()
            }
        }
    ) {
        Text(text = "Login")
    }
}

@Composable
@Preview
private fun Preview() {
    val state = UIState(
        email = "Email",
        password = "Password",
        isPendingLogin = false,
        viewEvent = null
    )
    Render(
        state = state,
        execute = {}
    )
}
