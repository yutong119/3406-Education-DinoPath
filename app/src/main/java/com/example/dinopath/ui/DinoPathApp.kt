package com.example.dinopath.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.dinopath.navigation.DinoDestination
import com.example.dinopath.navigation.bottomNavDestinations
import com.example.dinopath.ui.screens.CollectionScreen
import com.example.dinopath.ui.screens.ExhibitionScreen
import com.example.dinopath.ui.screens.HomeScreen
import com.example.dinopath.ui.screens.JournalScreen
import com.example.dinopath.ui.screens.SettingsScreen
import com.example.dinopath.ui.theme.DinoPathTheme

@Composable
fun DinoPathApp() {
    var currentDestination by rememberSaveable {
        mutableStateOf(DinoDestination.HOME)
    }

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
                    selected = destination == currentDestination,
                    onClick = {
                        currentDestination = destination
                    },
                )
            }
        },
    ) {
        when (currentDestination) {
            DinoDestination.HOME -> {
                HomeScreen()
            }

            DinoDestination.EXHIBITION -> {
                ExhibitionScreen()
            }

            DinoDestination.JOURNAL -> {
                JournalScreen()
            }

            DinoDestination.COLLECTION -> {
                CollectionScreen()
            }

            DinoDestination.SETTINGS -> {
                SettingsScreen()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun DinoPathAppPreview() {
    DinoPathTheme {
        DinoPathApp()
    }
}