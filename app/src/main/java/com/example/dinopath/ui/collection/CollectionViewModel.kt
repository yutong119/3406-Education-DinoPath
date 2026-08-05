package com.example.dinopath.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dinopath.domain.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repository: CollectionRepository,
) : ViewModel() {

    private val _removingSpecimenId = MutableStateFlow<String?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CollectionUiState> = combine(
        repository.observeFavourites(),
        _removingSpecimenId,
        _errorMessage,
    ) { favourites, removingId, error ->
        CollectionUiState(
            favourites = favourites,
            isLoading = false,
            removingSpecimenId = removingId,
            errorMessage = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CollectionUiState(),
    )

    fun removeFavourite(specimenId: String) {
        if (_removingSpecimenId.value != null) return

        viewModelScope.launch {
            _removingSpecimenId.value = specimenId
            _errorMessage.value = null
            try {
                repository.removeFavourite(specimenId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to remove specimen. Please try again."
            } finally {
                _removingSpecimenId.value = null
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
