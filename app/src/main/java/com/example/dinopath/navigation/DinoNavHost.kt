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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.dinopath.ui.knowledge.KnowledgeCheckViewModel
import com.example.dinopath.ui.home.HomeViewModel
import com.example.dinopath.ui.journal.JournalViewModel
import com.example.dinopath.ui.mistakes.MistakeReviewViewModel
import com.example.dinopath.ui.collection.CollectionViewModel
import com.example.dinopath.ui.screens.MistakeReviewScreen
import com.example.dinopath.ui.settings.SettingsViewModel

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
            val viewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                viewModel = viewModel,
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
            val viewModel: KnowledgeCheckViewModel =
                hiltViewModel()

            KnowledgeCheckScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onBackToLobby = {
                    navController.navigate(DinoDestination.HOME.route) {
                        popUpTo(DinoDestination.HOME.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(DinoDestination.JOURNAL.route) {
            val viewModel: JournalViewModel = hiltViewModel()

            JournalScreen(
                viewModel = viewModel,
                onReviewMistakes = {
                    navController.navigate(
                        AppRoutes.MISTAKE_REVIEW,
                    ) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(AppRoutes.MISTAKE_REVIEW) {
            val viewModel: MistakeReviewViewModel =
                hiltViewModel()

            MistakeReviewScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(DinoDestination.COLLECTION.route) {
            val viewModel: CollectionViewModel = hiltViewModel()
            CollectionScreen(viewModel = viewModel)
        }

        composable(DinoDestination.SETTINGS.route) {
            val viewModel: SettingsViewModel =
                hiltViewModel()

            SettingsScreen(
                viewModel = viewModel,
            )
        }
    }
}