package com.example.dinopath.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.dinopath.domain.model.UserPreferences
import com.example.dinopath.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userPreferencesDataStore
        by preferencesDataStore(
            name = "user_preferences",
        )

private object PreferenceKeys {
    val DARK_MODE =
        booleanPreferencesKey("dark_mode")

    val LARGE_TEXT =
        booleanPreferencesKey("large_text")

    val HIGH_CONTRAST =
        booleanPreferencesKey("high_contrast")

    val REDUCE_MOTION =
        booleanPreferencesKey("reduce_motion")
}

@Singleton
class DataStoreUserPreferencesRepository
@Inject constructor(
    @ApplicationContext
    private val context: Context,
) : UserPreferencesRepository {

    override val preferences:
            Flow<UserPreferences> =
        context.userPreferencesDataStore.data
            .map { values ->
                UserPreferences(
                    darkMode =
                        values[
                            PreferenceKeys.DARK_MODE
                        ] ?: true,
                    largeText =
                        values[
                            PreferenceKeys.LARGE_TEXT
                        ] ?: false,
                    highContrast =
                        values[
                            PreferenceKeys.HIGH_CONTRAST
                        ] ?: false,
                    reduceMotion =
                        values[
                            PreferenceKeys.REDUCE_MOTION
                        ] ?: false,
                )
            }

    override suspend fun setDarkMode(
        enabled: Boolean,
    ) {
        context.userPreferencesDataStore.edit {
            it[PreferenceKeys.DARK_MODE] =
                enabled
        }
    }

    override suspend fun setLargeText(
        enabled: Boolean,
    ) {
        context.userPreferencesDataStore.edit {
            it[PreferenceKeys.LARGE_TEXT] =
                enabled
        }
    }

    override suspend fun setHighContrast(
        enabled: Boolean,
    ) {
        context.userPreferencesDataStore.edit {
            it[PreferenceKeys.HIGH_CONTRAST] =
                enabled
        }
    }

    override suspend fun setReduceMotion(
        enabled: Boolean,
    ) {
        context.userPreferencesDataStore.edit {
            it[PreferenceKeys.REDUCE_MOTION] =
                enabled
        }
    }
}