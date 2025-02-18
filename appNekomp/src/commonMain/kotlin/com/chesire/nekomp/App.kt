package com.chesire.nekomp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.chesire.nekomp.feature.discover.ui.DiscoverScreen
import com.chesire.nekomp.feature.library.ui.LibraryScreen
import com.chesire.nekomp.feature.login.ui.LoginScreen
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    Logger.setTag("Nekomp")
    MyApplicationTheme {
        val navController = rememberNavController()
        var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.Library) }
        val isLoggedIn = !koinInject<AuthRepository>().accessTokenSync().isNullOrBlank()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) {
                    StartingPoint.LoggedIn.name
                } else {
                    StartingPoint.Login.name
                },
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = StartingPoint.Login.name) {
                    LoginScreen {
                        navController.navigate(StartingPoint.LoggedIn.name) {
                            // TODO Remove login from backstack
                        }
                    }
                }
                composable(route = StartingPoint.LoggedIn.name) {
                    NavigationSuiteScaffold(
                        navigationSuiteItems = {
                            AppDestinations.entries.forEach { destination ->
                                item(
                                    selected = destination == currentDestination,
                                    onClick = { currentDestination = destination },
                                    icon = {
                                        Icon(
                                            imageVector = destination.icon,
                                            contentDescription = stringResource(destination.contentDescription)
                                        )
                                    },
                                    label = {
                                        Text(text = stringResource(destination.label))
                                    }
                                )
                            }
                        }
                    ) {
                        when (currentDestination) {
                            AppDestinations.Library -> LibraryScreen()
                            AppDestinations.Discover -> DiscoverScreen()
                            AppDestinations.Profile -> LibraryScreen()
                            AppDestinations.Activity -> LibraryScreen()
                            AppDestinations.Settings -> LibraryScreen()
                        }
                    }
                }
            }
        }
    }
}
