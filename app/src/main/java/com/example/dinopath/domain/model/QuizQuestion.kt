package com.example.dinopath.domain.model

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String,
)