package com.chesire.nekomp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chesire.nekomp.feature.library.ui.LibraryScreen
import com.chesire.nekomp.feature.login.ui.LoginScreen
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MyApplicationTheme {
        val navController = rememberNavController()
        val isLoggedIn = !koinInject<AuthRepository>().accessTokenSync().isNullOrBlank()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) Route.Library.name else Route.Login.name,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = Route.Login.name) {
                    LoginScreen {
                        navController.navigate(Route.Library.name) {
                            // TODO Remove login from backstack
                        }
                    }
                }
                composable(route = Route.Library.name) {
                    LibraryScreen()
                }
            }
        }
    }
}
