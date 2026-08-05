package com.example.dinopath.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dinopath.domain.model.DinosaurSpecimen
import com.example.dinopath.domain.repository.CollectionRepository
import com.example.dinopath.domain.repository.LearningProgressRepository
import com.example.dinopath.domain.repository.SpecimenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val learningRepository: LearningProgressRepository,
    private val collectionRepository: CollectionRepository,
    private val specimenRepository: SpecimenRepository,
) : ViewModel() {

    private val localState =
        MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = combine(
        learningRepository.observeChapterProgress(),
        collectionRepository.observeIsFavourite(
            STEGOSAURUS.id,
        ),
        localState,
    ) { chapters, isFavourite, local ->
        local.copy(
            chapters = chapters,
            isLoading = false,
            isFeaturedFavourite = isFavourite,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    init {
        viewModelScope.launch {
            learningRepository.ensureInitialChapters()
        }

        loadFeaturedSpecimen()
    }

    fun loadFeaturedSpecimen(
        forceRefresh: Boolean = false,
    ) {
        viewModelScope.launch {
            localState.update { currentState ->
                currentState.copy(
                    isSpecimenLoading = true,
                    specimenError = null,
                )
            }

            specimenRepository
                .getSpecimenDetails(
                    queryTitle = STEGOSAURUS.name,
                    forceRefresh = forceRefresh,
                )
                .onSuccess { details ->
                    localState.update { currentState ->
                        currentState.copy(
                            specimenDetails = details,
                            isSpecimenLoading = false,
                            specimenError = null,
                        )
                    }
                }
                .onFailure { error ->
                    localState.update { currentState ->
                        currentState.copy(
                            isSpecimenLoading = false,
                            specimenError =
                                error.message
                                    ?: "Unable to load specimen information.",
                        )
                    }
                }
        }
    }

    fun toggleFeaturedFavourite() {
        if (uiState.value.isUpdatingFavourite) {
            return
        }

        viewModelScope.launch {
            localState.update { currentState ->
                currentState.copy(
                    isUpdatingFavourite = true,
                    favouriteError = null,
                )
            }

            runCatching {
                if (uiState.value.isFeaturedFavourite) {
                    collectionRepository.removeFavourite(
                        STEGOSAURUS.id,
                    )
                } else {
                    collectionRepository.addFavourite(
                        STEGOSAURUS,
                    )
                }
            }.onSuccess {
                localState.update { currentState ->
                    currentState.copy(
                        isUpdatingFavourite = false,
                    )
                }
            }.onFailure {
                localState.update { currentState ->
                    currentState.copy(
                        isUpdatingFavourite = false,
                        favouriteError =
                            "Failed to update collection. Please try again.",
                    )
                }
            }
        }
    }

    companion object {
        val STEGOSAURUS = DinosaurSpecimen(
            id = "stegosaurus",
            name = "Stegosaurus",
            period = "Late Jurassic",
            diet = "Herbivore",
            description =
                "One of the most recognisable dinosaurs, " +
                        "known for its large back plates and spiked tail.",
        )
    }
}