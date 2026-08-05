package com.example.dinopath.ui.mistakes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dinopath.domain.repository.LearningProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MistakeReviewViewModel @Inject constructor(
    private val repository: LearningProgressRepository,
) : ViewModel() {

    private val localState =
        MutableStateFlow(MistakeReviewUiState())

    val uiState = combine(
        repository.observeUnmasteredMistakes(),
        localState,
    ) { mistakes, local ->
        local.copy(
            mistakes = mistakes,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MistakeReviewUiState(),
    )

    fun markAsMastered(
        chapterId: Int,
        questionId: Int,
    ) {
        val key = "$chapterId-$questionId"

        viewModelScope.launch {
            localState.update {
                it.copy(
                    processingKey = key,
                    errorMessage = null,
                )
            }

            runCatching {
                repository.markMistakeMastered(
                    chapterId = chapterId,
                    questionId = questionId,
                )
            }.onSuccess {
                localState.update {
                    it.copy(processingKey = null)
                }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        processingKey = null,
                        errorMessage =
                            error.message
                                ?: "Unable to update this mistake.",
                    )
                }
            }
        }
    }
}