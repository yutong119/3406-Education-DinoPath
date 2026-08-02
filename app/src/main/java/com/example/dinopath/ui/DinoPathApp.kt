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
import com.example.dinopath.navigation.DinoDestination
import com.example.dinopath.navigation.DinoNavHost
import com.example.dinopath.navigation.bottomNavDestinations
import com.example.dinopath.ui.theme.DinoPathTheme

@Composable
fun DinoPathApp() {
    val navController = rememberNavController()

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        currentBackStackEntry?.destination?.route

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteItems = {
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
                    selected = currentRoute == destination.route,
                    onClick = {
                        if (currentRoute != destination.route) {
                            navController.navigate(destination.route) {
                                popUpTo(DinoDestination.HOME.route)
                                launchSingleTop = true
                            }
                        }
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
}

@PreviewScreenSizes
@Composable
private fun DinoPathAppPreview() {
    DinoPathTheme {
        DinoPathApp()
    }
}