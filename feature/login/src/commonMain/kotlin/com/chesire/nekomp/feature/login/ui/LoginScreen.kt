@file:OptIn(ExperimentalMaterial3Api::class)

package com.chesire.nekomp.feature.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.app_name
import nekomp.core.resources.generated.resources.login_create_account_link
import nekomp.core.resources.generated.resources.login_create_account_text
import nekomp.core.resources.generated.resources.login_cta
import nekomp.core.resources.generated.resources.login_email
import nekomp.core.resources.generated.resources.login_hide_password
import nekomp.core.resources.generated.resources.login_password
import nekomp.core.resources.generated.resources.login_show_password
import nekomp.core.resources.generated.resources.login_subtitle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

// TODO: Look at using Auth0 for Android (and others if can)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoggedIn: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Render(
        state = state,
        execute = { viewModel.execute(it) },
        onLoggedIn = onLoggedIn
    )
}

@Composable
private fun Render(
    state: UIState,
    execute: (ViewAction) -> Unit,
    onLoggedIn: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.viewEvent) {
        when (state.viewEvent) {
            is ViewEvent.LoginFailure -> {
                snackbarHostState.showSnackbar(
                    message = state.viewEvent.errorMessage,
                    duration = SnackbarDuration.Long
                )
            }

            ViewEvent.LoginSuccessful -> onLoggedIn()

            null -> Unit
        }

        execute(ViewAction.ObservedViewEvent)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(NekoRes.string.app_name)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(NekoRes.string.login_subtitle),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            UsernameInput(
                username = state.email,
                modifier = Modifier.fillMaxWidth(),
                onUsernameChanged = { execute(ViewAction.EmailUpdated(it)) }
            )
            PasswordInput(
                password = state.password,
                isLoggingIn = state.isPendingLogin,
                modifier = Modifier.fillMaxWidth(),
                onPasswordChanged = { execute(ViewAction.PasswordUpdated(it)) },
                onLoginPressed = { execute(ViewAction.LoginPressed) }
            )
            if (state.isPendingLogin) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else {
                LoginButton(
                    isLoggingIn = state.isPendingLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onLoginPressed = { execute(ViewAction.LoginPressed) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            CreateAccountLink()
        }
    }
}

@Composable
private fun UsernameInput(
    username: String,
    modifier: Modifier = Modifier,
    onUsernameChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChanged,
        modifier = modifier.semantics {
            contentType = ContentType.EmailAddress
        },
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
            Text(text = stringResource(NekoRes.string.login_email))
        }
    )
}

@Composable
private fun PasswordInput(
    password: String,
    isLoggingIn: Boolean,
    modifier: Modifier = Modifier,
    onPasswordChanged: (String) -> Unit,
    onLoginPressed: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    // TODO: Look at OutlinedSecureTextField
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        modifier = modifier.semantics {
            contentType = ContentType.Password
        },
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
                    contentDescription = stringResource(
                        if (passwordVisible) {
                            NekoRes.string.login_hide_password
                        } else {
                            NekoRes.string.login_show_password
                        }
                    )
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
            Text(text = stringResource(NekoRes.string.login_password))
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
        Text(text = stringResource(NekoRes.string.login_cta))
    }
}

@Composable
private fun CreateAccountLink(modifier: Modifier = Modifier) {
    val createAccountText = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url = stringResource(NekoRes.string.login_create_account_link),
                styles = TextLinkStyles(SpanStyle())
            )
        ) {
            append(stringResource(NekoRes.string.login_create_account_text))
        }
    }

    Text(
        text = createAccountText,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline)
    )
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
        execute = {},
        onLoggedIn = {}
    )
}
