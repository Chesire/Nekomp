package com.chesire.nekomp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.nav_airing
import nekomp.core.resources.generated.resources.nav_content_description_airing
import nekomp.core.resources.generated.resources.nav_content_description_discover
import nekomp.core.resources.generated.resources.nav_content_description_home
import nekomp.core.resources.generated.resources.nav_content_description_library
import nekomp.core.resources.generated.resources.nav_discover
import nekomp.core.resources.generated.resources.nav_home
import nekomp.core.resources.generated.resources.nav_library
import org.jetbrains.compose.resources.StringResource

enum class DashboardDestination(
    val label: StringResource,
    val contentDescription: StringResource,
    val icon: ImageVector
) {
    Home(
        label = NekoRes.string.nav_home,
        contentDescription = NekoRes.string.nav_content_description_home,
        icon = Icons.Default.Home
    ),
    Library(
        label = NekoRes.string.nav_library,
        contentDescription = NekoRes.string.nav_content_description_library,
        icon = Icons.Default.CollectionsBookmark
    ),
    Airing(
        label = NekoRes.string.nav_airing,
        contentDescription = NekoRes.string.nav_content_description_airing,
        icon = Icons.Default.DateRange
    ),
    Discover(
        label = NekoRes.string.nav_discover,
        contentDescription = NekoRes.string.nav_content_description_discover,
        icon = Icons.Default.Search
    )
}
