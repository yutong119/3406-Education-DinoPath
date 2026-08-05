package com.example.dinopath.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dinopath.domain.model.UserPreferences
import com.example.dinopath.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppPreferencesViewModel @Inject constructor(
    repository: UserPreferencesRepository,
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
}