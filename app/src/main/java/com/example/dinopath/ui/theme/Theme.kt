package com.example.dinopath.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val DinoPathDarkColorScheme =
    darkColorScheme(
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
        surfaceVariant =
            DarkMuseumSurfaceVariant,
        onSurfaceVariant =
            LightMuseumSurfaceVariant,
        error = ErrorRed,
    )

private val DinoPathLightColorScheme =
    lightColorScheme(
        primary = MuseumGreen,
        onPrimary = WarmIvory,
        primaryContainer = SoftGreen,
        onPrimaryContainer =
            DeepForestGreen,
        secondary = FossilBrown,
        onSecondary = WarmIvory,
        tertiary = DarkAmberGold,
        onTertiary = DeepForestGreen,
        background = WarmIvory,
        onBackground = DeepForestGreen,
        surface = LightMuseumSurface,
        onSurface = DeepForestGreen,
        surfaceVariant =
            LightMuseumSurfaceVariant,
        onSurfaceVariant = FossilBrown,
        error = ErrorRed,
    )

private fun largeTypography(
    base: Typography,
): Typography {
    return base.copy(
        bodyLarge = base.bodyLarge.copy(
            fontSize = 19.sp,
        ),
        bodyMedium = base.bodyMedium.copy(
            fontSize = 17.sp,
        ),
        bodySmall = base.bodySmall.copy(
            fontSize = 15.sp,
        ),
        titleMedium = base.titleMedium.copy(
            fontSize = 20.sp,
        ),
        titleLarge = base.titleLarge.copy(
            fontSize = 25.sp,
        ),
        headlineSmall =
            base.headlineSmall.copy(
                fontSize = 28.sp,
            ),
        headlineLarge =
            base.headlineLarge.copy(
                fontSize = 36.sp,
            ),
    )
}

@Composable
fun DinoPathTheme(
    darkTheme: Boolean = true,
    highContrast: Boolean = false,
    largeText: Boolean = false,
    reduceMotion: Boolean = false,
    content: @Composable () -> Unit,
) {
    val baseScheme =
        if (darkTheme) {
            DinoPathDarkColorScheme
        } else {
            DinoPathLightColorScheme
        }

    val selectedScheme =
        if (highContrast) {
            baseScheme.copy(
                surface = baseScheme.background,
                onSurface =
                    baseScheme.onBackground,
                outline =
                    baseScheme.onBackground,
            )
        } else {
            baseScheme
        }

    CompositionLocalProvider(
        LocalReduceMotion provides reduceMotion,
    ) {
        MaterialTheme(
            colorScheme = selectedScheme,
            typography = if (largeText) {
                largeTypography(Typography)
            } else {
                Typography
            },
            content = content,
        )
    }
}