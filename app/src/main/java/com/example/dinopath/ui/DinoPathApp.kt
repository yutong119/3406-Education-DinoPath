package com.example.dinopath.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dinopath.navigation.DinoNavHost
import com.example.dinopath.navigation.bottomNavDestinations
import com.example.dinopath.navigation.navigateToTopLevelDestination
import com.example.dinopath.ui.theme.DinoPathTheme

@Composable
fun DinoPathApp() {
    val navController = rememberNavController()

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        currentBackStackEntry?.destination?.route

    val bottomRoutes = bottomNavDestinations
        .map { destination ->
            destination.route
        }
        .toSet()

    val showNavigationSuite =
        currentRoute in bottomRoutes

    /*
     * Do not create NavigationSuiteScaffold at all on Welcome,
     * Knowledge Check, Result, and other non-top-level screens.
     *
     * This removes the empty navigation container that previously
     * appeared at the bottom of the Welcome screen.
     */
    if (showNavigationSuite) {
        NavigationSuiteScaffold(
            modifier = Modifier.fillMaxSize(),
            navigationSuiteItems = {
                bottomNavDestinations.forEach { destination ->
                    val isSelected =
                        currentRoute == destination.route

                    item(
                        icon = {
                            Icon(
                                imageVector = if (isSelected) {
                                    destination.selectedIcon
                                } else {
                                    destination.unselectedIcon
                                },
                                contentDescription =
                                    destination.label,
                            )
                        },
                        label = {
                            Text(
                                text = destination.label,
                                style =
                                    MaterialTheme.typography
                                        .labelMedium,
                                fontWeight = if (isSelected) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            navController
                                .navigateToTopLevelDestination(
                                    destination = destination,
                                    currentRoute = currentRoute,
                                )
                        },
                    )
                }
            },
        ) {
            DinoNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
            )
        }
    } else {
        /*
         * Welcome and secondary flow screens use the full window.
         * No navigation bar, navigation rail, or empty navigation
         * container is created.
         */
        DinoNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@PreviewScreenSizes
@Composable
private fun DinoPathAppPreview() {
    DinoPathTheme {
        DinoPathApp()
    }
}