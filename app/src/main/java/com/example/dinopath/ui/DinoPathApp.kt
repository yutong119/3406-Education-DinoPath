package com.example.dinopath.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
     * Keep exactly one NavHost for the whole app.
     *
     * For Welcome and Knowledge Check, this scaffold remains present,
     * but no navigation items are added, so the bottom navigation is hidden.
     */
    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteItems = {
            if (showNavigationSuite) {
                bottomNavDestinations.forEach { destination ->
                    item(
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                            )
                        },
                        label = {
                            Text(destination.label)
                        },
                        selected =
                            currentRoute == destination.route,
                        onClick = {
                            navController.navigateToTopLevelDestination(
                                destination = destination,
                                currentRoute = currentRoute,
                            )
                        },
                    )
                }
            }
        },
    ) {
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