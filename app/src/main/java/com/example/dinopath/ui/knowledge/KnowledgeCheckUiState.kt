package com.example.dinopath.ui.knowledge

import com.example.dinopath.domain.model.QuizQuestion

data class KnowledgeCheckUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val hasSubmitted: Boolean = false,
    val score: Int = 0,
    val isComplete: Boolean = false,
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
        get() = when {
            questions.isEmpty() -> 0
            score == questions.size -> 3
            score == questions.size - 1 -> 2
            else -> 1
        }

    val accuracy: Int
        get() {
            if (questions.isEmpty()) {
                return 0
            }

            return (
                    score.toFloat() /
                            questions.size.toFloat() *
                            100
                    ).toInt()
        }
}