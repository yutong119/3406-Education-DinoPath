package com.example.dinopath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dinopath.ui.DinoPathApp
import com.example.dinopath.ui.app.AppPreferencesViewModel
import com.example.dinopath.ui.theme.DinoPathTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DinoPathRoot()
        }
    }
}

@Composable
private fun DinoPathRoot() {
    val viewModel:
            AppPreferencesViewModel =
        hiltViewModel()

    val preferences by
    viewModel.preferences
        .collectAsStateWithLifecycle()

    DinoPathTheme(
        darkTheme = preferences.darkMode,
        highContrast =
            preferences.highContrast,
        largeText = preferences.largeText,
        reduceMotion =
            preferences.reduceMotion,
    ) {
        DinoPathApp()
    }
}