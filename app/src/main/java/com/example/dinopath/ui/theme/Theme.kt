package com.example.dinopath.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DinoPathDarkColorScheme = darkColorScheme(
    primary = AmberGold,
    onPrimary = DeepForestGreen,
    primaryContainer = MuseumGreen,
    onPrimaryContainer = WarmIvory,

    secondary = SoftGreen,
    onSecondary = DeepForestGreen,

    tertiary = FossilBrown,
    onTertiary = WarmIvory,

    background = ForestGreen,
    onBackground = WarmIvory,

    surface = DarkMuseumSurface,
    onSurface = WarmIvory,

    surfaceVariant = DarkMuseumSurfaceVariant,
    onSurfaceVariant = LightMuseumSurfaceVariant,

    error = ErrorRed,
)

private val DinoPathLightColorScheme = lightColorScheme(
    primary = MuseumGreen,
    onPrimary = WarmIvory,
    primaryContainer = SoftGreen,
    onPrimaryContainer = DeepForestGreen,

    secondary = FossilBrown,
    onSecondary = WarmIvory,

    tertiary = DarkAmberGold,
    onTertiary = DeepForestGreen,

    background = WarmIvory,
    onBackground = DeepForestGreen,

    surface = LightMuseumSurface,
    onSurface = DeepForestGreen,

    surfaceVariant = LightMuseumSurfaceVariant,
    onSurfaceVariant = FossilBrown,

    error = ErrorRed,
)

@Composable
fun DinoPathTheme(
    // The final Settings screen will control this value using DataStore.
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DinoPathDarkColorScheme
        } else {
            DinoPathLightColorScheme
        },
        typography = Typography,
        content = content,
    )
}