package com.example.dinopath.ui.knowledge

import com.example.dinopath.domain.model.QuizQuestion
import com.example.dinopath.domain.scoring.calculateAccuracy
import com.example.dinopath.domain.scoring.calculateStars

data class KnowledgeCheckUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val hasSubmitted: Boolean = false,
    val score: Int = 0,
    val isComplete: Boolean = false,
    val submittedAnswers: Map<Int, String> = emptyMap(),
    val earnedStars: Int = 0,
    val savedAccuracy: Int = 0,
    val isSaving: Boolean = false,
    val saveError: String? = null,
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val questionNumber: Int
        get() = currentQuestionIndex + 1

    val totalQuestions: Int
        get() = questions.size

    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questions.lastIndex

    val stars: Int
        get() = calculateStars(
            score = score,
            totalQuestions = totalQuestions,
        )

    val accuracy: Int
        get() = calculateAccuracy(
            score = score,
            totalQuestions = totalQuestions,
        )
}