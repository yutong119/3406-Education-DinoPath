package com.example.dinopath.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dinopath.domain.repository.LearningProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    repository: LearningProgressRepository,
) : ViewModel() {

    val uiState = combine(
        repository.observeChapterProgress(),
        repository.observeQuizHistory(),
        repository.observeUnmasteredMistakes(),
    ) { chapters, quizHistory, mistakes ->
        JournalUiState(
            chapters = chapters,
            quizHistory = quizHistory,
            mistakes = mistakes,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = JournalUiState(),
    )
}