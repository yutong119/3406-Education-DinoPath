package com.example.dinopath.domain.model

data class MistakeSummary(
    val chapterId: Int,
    val questionId: Int,
    val question: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val explanation: String,
    val isMastered: Boolean,
)