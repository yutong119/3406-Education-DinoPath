package com.example.dinopath.domain.model

data class QuizHistory(
    val resultId: Long,
    val chapterId: Int,
    val score: Int,
    val totalQuestions: Int,
    val accuracy: Int,
    val stars: Int,
    val completedAt: Long,
)