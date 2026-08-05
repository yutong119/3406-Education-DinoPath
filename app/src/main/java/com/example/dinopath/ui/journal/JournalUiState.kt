package com.example.dinopath.ui.journal

import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.ChapterStatus
import com.example.dinopath.domain.model.MistakeSummary
import com.example.dinopath.domain.model.QuizHistory

data class JournalUiState(
    val chapters: List<ChapterProgress> = emptyList(),
    val quizHistory: List<QuizHistory> = emptyList(),
    val mistakes: List<MistakeSummary> = emptyList(),
    val isLoading: Boolean = true,
) {
    val completedChapters: Int
        get() = chapters.count {
            it.status == ChapterStatus.COMPLETED
        }

    val totalChapters: Int
        get() = chapters.size

    val totalStars: Int
        get() = chapters.sumOf { it.stars }

    val averageAccuracy: Int
        get() {
            if (quizHistory.isEmpty()) {
                return 0
            }

            return quizHistory
                .map { it.accuracy }
                .average()
                .toInt()
        }

    val mistakeCount: Int
        get() = mistakes.size

    val recentActivity: List<QuizHistory>
        get() = quizHistory.take(5)
}