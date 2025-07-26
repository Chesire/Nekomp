package com.chesire.nekomp.feature.library.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.ui.graphics.vector.ImageVector
import com.chesire.nekomp.core.resources.NekoRes
import nekomp.core.resources.generated.resources.library_view_type_card
import nekomp.core.resources.generated.resources.library_view_type_grid
import nekomp.core.resources.generated.resources.library_view_type_list
import org.jetbrains.compose.resources.StringResource

enum class ViewType(val displayString: StringResource, val icon: ImageVector) {
    Card(NekoRes.string.library_view_type_card, Icons.Default.ViewAgenda),
    List(NekoRes.string.library_view_type_list, Icons.AutoMirrored.Filled.ViewList),
    Grid(NekoRes.string.library_view_type_grid, Icons.Default.GridView);

    companion object {

        internal val default: ViewType = Card

        fun fromString(input: String): ViewType {
            return ViewType.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}
