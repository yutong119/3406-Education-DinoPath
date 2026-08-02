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
import com.example.dinopath.ui.screens.KnowledgeCheckScreen
import com.example.dinopath.ui.screens.SettingsScreen
import com.example.dinopath.ui.screens.WelcomeScreen

@Composable
fun DinoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.WELCOME,
        modifier = modifier,
    ) {
        composable(AppRoutes.WELCOME) {
            WelcomeScreen(
                onEnterMuseum = {
                    navController.navigate(
                        DinoDestination.HOME.route,
                    ) {
                        popUpTo(AppRoutes.WELCOME) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
            )
        }

        /*
         * Home must be registered exactly once.
         */
        composable(DinoDestination.HOME.route) {
            HomeScreen(
                onContinueExpedition = {
                    navController.navigate(
                        DinoDestination.EXHIBITION.route,
                    ) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(DinoDestination.EXHIBITION.route) {
            ExhibitionScreen(
                onStartKnowledgeCheck = {
                    navController.navigate(
                        AppRoutes.KNOWLEDGE_CHECK,
                    ) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(AppRoutes.KNOWLEDGE_CHECK) {
            KnowledgeCheckScreen(
                onBack = {
                    navController.popBackStack()
                },
            )
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