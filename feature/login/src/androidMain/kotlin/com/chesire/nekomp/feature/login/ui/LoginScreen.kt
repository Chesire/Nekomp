package com.chesire.nekomp.feature.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.viewEvent) {
        when (state.viewEvent) {
            is ViewEvent.LoginFailure -> Timber.d("Login successful")
            ViewEvent.LoginSuccessful -> Timber.w("Login failure")
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
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = state.email,
            onValueChange = { execute(ViewAction.EmailUpdated(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Email")
            }
        )
        TextField(
            value = state.password,
            onValueChange = { execute(ViewAction.PasswordUpdated(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Password")
            }
        )
        Button(
            onClick = { execute(ViewAction.LoginPressed) },
            enabled = !state.isPendingLogin
        ) {
            Text("Login")
        }
    }
}
