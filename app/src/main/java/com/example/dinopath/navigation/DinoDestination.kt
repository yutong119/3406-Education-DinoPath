package com.example.dinopath.navigation

/**
 * The five top-level sections displayed in DinoPath's navigation suite.
 */
enum class DinoDestination(
    val route: String,
    val label: String,
) {
    HOME(
        route = "home",
        label = "Home",
    ),

    EXHIBITION(
        route = "exhibition",
        label = "Exhibition",
    ),

    JOURNAL(
        route = "journal",
        label = "Journal",
    ),

    COLLECTION(
        route = "collection",
        label = "Collection",
    ),

    SETTINGS(
        route = "settings",
        label = "Settings",
    ),
}

val bottomNavDestinations = DinoDestination.entries