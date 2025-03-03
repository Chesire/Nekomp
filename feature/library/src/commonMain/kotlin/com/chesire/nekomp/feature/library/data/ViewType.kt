package com.chesire.nekomp.feature.library.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.ui.graphics.vector.ImageVector

enum class ViewType(val title: String, val icon: ImageVector) {
    List(title = "List", icon = Icons.AutoMirrored.Filled.ViewList),
    Card(title = "Card", icon = Icons.Default.ViewAgenda),
    Grid(title = "Grid", icon = Icons.Default.GridView);

    companion object {

        internal val default: ViewType = Card

        fun fromString(input: String): ViewType {
            return ViewType.entries.find { it.name.lowercase() == input.lowercase() } ?: default
        }
    }
}
