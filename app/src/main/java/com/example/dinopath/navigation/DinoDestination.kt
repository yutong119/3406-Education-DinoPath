package com.example.dinopath.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class DinoDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME(
        route = "home",
        label = "Home",
        icon = Icons.Outlined.Home,
    ),

    EXHIBITION(
        route = "exhibition",
        label = "Exhibition",
        icon = Icons.Outlined.Explore,
    ),

    JOURNAL(
        route = "journal",
        label = "Journal",
        icon = Icons.Outlined.MenuBook,
    ),

    COLLECTION(
        route = "collection",
        label = "Collection",
        icon = Icons.Outlined.FavoriteBorder,
    ),

    SETTINGS(
        route = "settings",
        label = "Settings",
        icon = Icons.Outlined.Settings,
    ),
}

val bottomNavDestinations = DinoDestination.entries