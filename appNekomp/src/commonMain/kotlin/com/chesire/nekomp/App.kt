@file:OptIn(ExperimentalComposeUiApi::class)

package com.chesire.nekomp

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.chesire.nekomp.core.preferences.ApplicationSettings
import com.chesire.nekomp.core.preferences.Theme
import com.chesire.nekomp.core.ui.NekompTheme
import com.chesire.nekomp.feature.airing.ui.AiringScreen
import com.chesire.nekomp.feature.discover.ui.DiscoverScreen
import com.chesire.nekomp.feature.home.ui.HomeScreen
import com.chesire.nekomp.feature.library.ui.LibraryScreen
import com.chesire.nekomp.feature.login.ui.LoginScreen
import com.chesire.nekomp.feature.profile.ui.ProfileScreen
import com.chesire.nekomp.feature.settings.ui.SettingsScreen
import com.chesire.nekomp.library.datasource.auth.AuthRepository
import com.chesire.nekomp.navigation.DashboardDestination
import com.chesire.nekomp.navigation.OriginScreen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    Logger.setTag("Nekomp")

    val appSettings = koinInject<ApplicationSettings>()
    val theme by appSettings.theme.collectAsState(Theme.System)

    val useDarkTheme = when {
        theme == Theme.System && isSystemInDarkTheme() -> true
        theme == Theme.System && !isSystemInDarkTheme() -> false
        theme == Theme.Light -> false
        theme == Theme.Dark -> true
        else -> false
    }

    NekompTheme(useDarkTheme) {
        val appNavController = rememberNavController()
        val isLoggedIn = !koinInject<AuthRepository>().accessTokenSync().isNullOrBlank()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = appNavController,
                startDestination = if (isLoggedIn) {
                    OriginScreen.Dashboard.name
                } else {
                    OriginScreen.Login.name
                },
                popExitTransition = {
                    scaleOut(
                        targetScale = 0.9f,
                        transformOrigin = TransformOrigin(
                            pivotFractionX = 0.5f,
                            pivotFractionY = 0.5f
                        )
                    )
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                modifier = Modifier.fillMaxSize()
            ) {
                addLogin(appNavController)
                addDashboard(appNavController)
                addProfile(appNavController)
                addSettings(appNavController)
            }
        }
    }
}

private fun NavGraphBuilder.addLogin(appNavController: NavController) {
    composable(route = OriginScreen.Login.name) {
        LoginScreen(
            onLoggedIn = {
                appNavController.navigate(OriginScreen.Dashboard.name) {
                    popUpTo(OriginScreen.Login.name) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.addProfile(appNavController: NavController) {
    composable(route = OriginScreen.Profile.name) {
        ProfileScreen(
            goBack = { appNavController.popBackStack() },
            navigateToSettings = {
                appNavController.navigate(OriginScreen.Settings.name)
            }
        )
    }
}

private fun NavGraphBuilder.addSettings(appNavController: NavController) {
    composable(route = OriginScreen.Settings.name) {
        SettingsScreen(
            goBack = { appNavController.popBackStack() },
            onLoggedOut = {
                appNavController.navigate(OriginScreen.Login.name) {
                    popUpTo(OriginScreen.Dashboard.name) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.addDashboard(appNavController: NavController) {
    composable(route = OriginScreen.Dashboard.name) {
        val dashboardNavController = rememberNavController()
        val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                DashboardDestination.entries.forEach { destination ->
                    item(
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(destination.name, null)
                        } == true,
                        onClick = {
                            dashboardNavController.navigate(destination.name) {
                                popUpTo(dashboardNavController.graph.findStartDestination().route!!) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
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
            NavHost(
                navController = dashboardNavController,
                startDestination = DashboardDestination.Home.name
            ) {
                composable(route = DashboardDestination.Home.name) {
                    HomeScreen(
                        navigateToProfile = {
                            appNavController.navigate(OriginScreen.Profile.name)
                        }
                    )
                }
                composable(route = DashboardDestination.Library.name) {
                    LibraryScreen()
                }
                composable(route = DashboardDestination.Airing.name) {
                    AiringScreen()
                }
                composable(route = DashboardDestination.Discover.name) {
                    DiscoverScreen()
                }
            }
        }
    }
}
