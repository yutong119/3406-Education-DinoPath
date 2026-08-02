package com.example.dinopath.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dinopath.ui.screens.CollectionScreen
import com.example.dinopath.ui.screens.ExhibitionScreen
import com.example.dinopath.ui.screens.HomeScreen
import com.example.dinopath.ui.screens.JournalScreen
import com.example.dinopath.ui.screens.SettingsScreen

@Composable
fun DinoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DinoDestination.HOME.route,
        modifier = modifier,
    ) {
        composable(DinoDestination.HOME.route) {
            HomeScreen()
        }

        composable(DinoDestination.EXHIBITION.route) {
            ExhibitionScreen()
        }

        composable(DinoDestination.JOURNAL.route) {
            JournalScreen()
        }

        composable(DinoDestination.COLLECTION.route) {
            CollectionScreen()
        }

        composable(DinoDestination.SETTINGS.route) {
            SettingsScreen()
        }
    }
}