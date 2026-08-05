package com.example.dinopath.domain.repository

import com.example.dinopath.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val preferences: Flow<UserPreferences>

    suspend fun setDarkMode(enabled: Boolean)

    suspend fun setLargeText(enabled: Boolean)

    suspend fun setHighContrast(enabled: Boolean)

    suspend fun setReduceMotion(enabled: Boolean)
}