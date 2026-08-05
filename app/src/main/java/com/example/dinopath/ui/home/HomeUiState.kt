package com.example.dinopath.ui.home

import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.ChapterStatus

data class HomeUiState(
    val chapters: List<ChapterProgress> = emptyList(),
    val isLoading: Boolean = true,
    val isFeaturedFavourite: Boolean = false,
    val isUpdatingFavourite: Boolean = false,
    val favouriteError: String? = null,
) {
    val totalStars: Int
        get() = chapters.sumOf { it.stars }

    val currentChapter: ChapterProgress?
        get() = chapters.firstOrNull {
            it.isUnlocked && it.status != ChapterStatus.COMPLETED
        }
}
