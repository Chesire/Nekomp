package com.chesire.nekomp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.nav_activity
import nekomp.core.resources.generated.resources.nav_airing
import nekomp.core.resources.generated.resources.nav_content_description_activity
import nekomp.core.resources.generated.resources.nav_content_description_airing
import nekomp.core.resources.generated.resources.nav_content_description_discover
import nekomp.core.resources.generated.resources.nav_content_description_library
import nekomp.core.resources.generated.resources.nav_content_description_profile
import nekomp.core.resources.generated.resources.nav_content_description_settings
import nekomp.core.resources.generated.resources.nav_discover
import nekomp.core.resources.generated.resources.nav_library
import nekomp.core.resources.generated.resources.nav_profile
import nekomp.core.resources.generated.resources.nav_settings
import org.jetbrains.compose.resources.StringResource

enum class AppDestinations(
    val label: StringResource,
    val contentDescription: StringResource,
    val icon: ImageVector
) {
    Library(
        label = NekoRes.string.nav_library,
        contentDescription = NekoRes.string.nav_content_description_library,
        icon = Icons.Default.Info
    ),
    Discover(
        label = NekoRes.string.nav_discover,
        contentDescription = NekoRes.string.nav_content_description_discover,
        icon = Icons.Default.Star
    ),
    Airing(
        // TODO: Show when shows are airing each season
        label = NekoRes.string.nav_airing,
        contentDescription = NekoRes.string.nav_content_description_airing,
        icon = Icons.Default.Air
    ),
    Profile(
        // TODO: Users profile, with stats
        label = NekoRes.string.nav_profile,
        contentDescription = NekoRes.string.nav_content_description_profile,
        icon = Icons.Default.AccountCircle
    ),
    Activity(
        // TODO: Users activity (watched show, rated show, etc)
        label = NekoRes.string.nav_activity,
        contentDescription = NekoRes.string.nav_content_description_activity,
        icon = Icons.Default.DateRange
    ),
    Settings(
        // TODO: Application settings, links to github etc
        label = NekoRes.string.nav_settings,
        contentDescription = NekoRes.string.nav_content_description_settings,
        icon = Icons.Default.Settings
    )
}
