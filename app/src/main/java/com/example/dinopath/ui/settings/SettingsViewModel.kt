package com.example.dinopath.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dinopath.domain.model.UserPreferences
import com.example.dinopath.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository:
    UserPreferencesRepository,
) : ViewModel() {

    val preferences =
        repository.preferences.stateIn(
            scope = viewModelScope,
            started =
                SharingStarted.WhileSubscribed(
                    5_000,
                ),
            initialValue = UserPreferences(),
        )

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDarkMode(enabled)
        }
    }

    fun setLargeText(enabled: Boolean) {
        viewModelScope.launch {
            repository.setLargeText(enabled)
        }
    }

    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch {
            repository.setHighContrast(enabled)
        }
    }

    fun setReduceMotion(enabled: Boolean) {
        viewModelScope.launch {
            repository.setReduceMotion(enabled)
        }
    }
}