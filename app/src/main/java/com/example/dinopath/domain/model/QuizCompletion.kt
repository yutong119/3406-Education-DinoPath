package com.example.dinopath.domain.model

data class QuizCompletion(
    val chapterId: Int,
    val score: Int,
    val totalQuestions: Int,
    val accuracy: Int,
    val stars: Int,
)