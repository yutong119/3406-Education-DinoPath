package com.example.dinopath.domain.model

enum class ChapterStatus {
    COMPLETED,
    IN_PROGRESS,
    LOCKED,
}

object ChapterIds {
    const val MEET_THE_DINOSAURS = 1
    const val TRIASSIC_PERIOD = 2
    const val JURASSIC_PERIOD = 3
    const val CRETACEOUS_PERIOD = 4
    const val HABITATS_AND_DIETS = 5
    const val MASS_EXTINCTION = 6
    const val MODERN_BIRDS = 7
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