package com.example.dinopath.domain.model

enum class ChapterStatus {
    COMPLETED,
    IN_PROGRESS,
    LOCKED,
}

data class ChapterProgress(
    val chapterId: Int,
    val chapterOrder: Int,
    val title: String,
    val subtitle: String,
    val status: ChapterStatus,
    val isUnlocked: Boolean,
    val stars: Int,
    val bestScore: Int,
    val totalQuestions: Int,
    val bestAccuracy: Int,
)