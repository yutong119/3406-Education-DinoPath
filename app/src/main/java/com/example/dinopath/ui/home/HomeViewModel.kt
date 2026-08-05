package com.example.dinopath.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dinopath.domain.model.DinosaurSpecimen
import com.example.dinopath.domain.repository.CollectionRepository
import com.example.dinopath.domain.repository.LearningProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val learningRepository: LearningProgressRepository,
    private val collectionRepository: CollectionRepository,
) : ViewModel() {

    private val _isUpdatingFavourite = MutableStateFlow(false)
    private val _favouriteError = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            learningRepository.ensureInitialChapters()
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        learningRepository.observeChapterProgress(),
        collectionRepository.observeIsFavourite(STEGOSAURUS.id),
        _isUpdatingFavourite,
        _favouriteError
    ) { chapters, isFavourite, isUpdating, error ->
        HomeUiState(
            chapters = chapters,
            isLoading = false,
            isFeaturedFavourite = isFavourite,
            isUpdatingFavourite = isUpdating,
            favouriteError = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    fun toggleFeaturedFavourite() {
        if (_isUpdatingFavourite.value) return

        viewModelScope.launch {
            _isUpdatingFavourite.value = true
            _favouriteError.value = null
            try {
                if (uiState.value.isFeaturedFavourite) {
                    collectionRepository.removeFavourite(STEGOSAURUS.id)
                } else {
                    collectionRepository.addFavourite(STEGOSAURUS)
                }
            } catch (e: Exception) {
                _favouriteError.value = "Failed to update collection. Please try again."
            } finally {
                _isUpdatingFavourite.value = false
            }
        }
    }

    companion object {
        val STEGOSAURUS = DinosaurSpecimen(
            id = "stegosaurus",
            name = "Stegosaurus",
            period = "Late Jurassic",
            diet = "Herbivore",
            description = "One of the most recognisable dinosaurs, known for its large back plates and spiked tail."
        )
    }
}
