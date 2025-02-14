package com.chesire.nekomp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.nav_activity
import nekomp.core.resources.generated.resources.nav_content_description_activity
import nekomp.core.resources.generated.resources.nav_content_description_library
import nekomp.core.resources.generated.resources.nav_content_description_profile
import nekomp.core.resources.generated.resources.nav_content_description_settings
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
    Profile(
        label = NekoRes.string.nav_profile,
        contentDescription = NekoRes.string.nav_content_description_profile,
        icon = Icons.Default.AccountCircle
    ),
    Activity(
        label = NekoRes.string.nav_activity,
        contentDescription = NekoRes.string.nav_content_description_activity,
        icon = Icons.Default.DateRange
    ),
    Settings(
        label = NekoRes.string.nav_settings,
        contentDescription = NekoRes.string.nav_content_description_settings,
        icon = Icons.Default.Settings
    )
}
