package com.example.dinopath.ui.home

import com.example.dinopath.domain.model.ChapterProgress

data class HomeUiState(
    val chapters: List<ChapterProgress> = emptyList(),
    val isLoading: Boolean = true,
) {
    val totalStars: Int
        get() = chapters.sumOf { it.stars }

    val currentChapter: ChapterProgress?
        get() = chapters.firstOrNull {
            it.isUnlocked &&
                    it.status !=
                    com.example.dinopath.domain.model.ChapterStatus.COMPLETED
        }
}