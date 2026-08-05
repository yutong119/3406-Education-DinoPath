package com.example.dinopath.ui.mistakes

import com.example.dinopath.domain.model.MistakeSummary

data class MistakeReviewUiState(
    val mistakes: List<MistakeSummary> = emptyList(),
    val isLoading: Boolean = true,
    val processingKey: String? = null,
    val errorMessage: String? = null,
)