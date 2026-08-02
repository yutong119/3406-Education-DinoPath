package com.example.dinopath.navigation

import androidx.navigation.NavHostController

/**
 * Navigates between DinoPath's five top-level destinations.
 *
 * Each destination keeps one instance, and its saveable state is restored
 * when the user returns to it.
 */
fun NavHostController.navigateToTopLevelDestination(
    destination: DinoDestination,
    currentRoute: String?,
) {
    if (currentRoute == destination.route) {
        return
    }

    navigate(destination.route) {
        popUpTo(DinoDestination.HOME.route) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}