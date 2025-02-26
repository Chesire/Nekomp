package com.chesire.nekomp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.Theme
import com.chesire.nekomp.feature.discover.ui.DiscoverScreen
import com.chesire.nekomp.feature.home.ui.HomeScreen
import com.chesire.nekomp.feature.library.ui.LibraryScreen
import com.chesire.nekomp.feature.login.ui.LoginScreen
import com.chesire.nekomp.feature.profile.ui.ProfileScreen
import com.chesire.nekomp.feature.settings.ui.SettingsScreen
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    Logger.setTag("Nekomp")

    val applicationSettings = getKoin().get<ApplicationSettings>()
    val theme by applicationSettings.theme.collectAsState(Theme.System)

    NekompTheme(theme = theme) {
        val navController = rememberNavController()
        var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.Home) }
        val isLoggedIn = !koinInject<AuthRepository>().accessTokenSync().isNullOrBlank()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) {
                    OriginScreen.Dashboard.name
                } else {
                    OriginScreen.Login.name
                },
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = OriginScreen.Login.name) {
                    LoginScreen {
                        navController.navigate(OriginScreen.Dashboard.name) {
                            popUpTo(OriginScreen.Login.name) {
                                inclusive = true
                            }
                        }
                    }
                }
                composable(route = OriginScreen.Dashboard.name) {
                    NavigationSuiteScaffold(
                        navigationSuiteItems = {
                            AppDestinations
                                .entries
                                .forEach { destination ->
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
                            AppDestinations.Home -> HomeScreen {
                                navController.navigate(OriginScreen.Profile.name)
                            }

                            AppDestinations.Library -> LibraryScreen()
                            AppDestinations.Airing -> LibraryScreen()
                            AppDestinations.Discover -> DiscoverScreen()
                        }
                    }
                }
                composable(route = OriginScreen.Profile.name) {
                    ProfileScreen {
                        navController.navigate(OriginScreen.Settings.name)
                    }
                }
                composable(route = OriginScreen.Settings.name) {
                    SettingsScreen {
                        navController.navigate(OriginScreen.Login.name) {
                            popUpTo(OriginScreen.Dashboard.name) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }
}
