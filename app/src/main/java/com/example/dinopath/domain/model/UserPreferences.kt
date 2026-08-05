package com.example.dinopath.domain.model

data class UserPreferences(
    val darkMode: Boolean = true,
    val largeText: Boolean = false,
    val highContrast: Boolean = false,
    val reduceMotion: Boolean = false,
)