package com.example.dinopath.ui.knowledge

import androidx.lifecycle.ViewModel
import com.example.dinopath.domain.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class KnowledgeCheckViewModel @Inject constructor(
    quizRepository: QuizRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        KnowledgeCheckUiState(
            questions = quizRepository.getJurassicQuestions(),
        ),
    )

    val uiState: StateFlow<KnowledgeCheckUiState> =
        _uiState.asStateFlow()

    fun selectAnswer(answer: String) {
        _uiState.update { currentState ->
            if (currentState.hasSubmitted) {
                currentState
            } else {
                currentState.copy(
                    selectedAnswer = answer,
                )
            }
        }
    }

    fun submitAnswer() {
        _uiState.update { currentState ->
            val question =
                currentState.currentQuestion
                    ?: return@update currentState

            val selectedAnswer =
                currentState.selectedAnswer
                    ?: return@update currentState

            if (currentState.hasSubmitted) {
                return@update currentState
            }

            val updatedScore =
                if (selectedAnswer == question.correctAnswer) {
                    currentState.score + 1
                } else {
                    currentState.score
                }

            currentState.copy(
                hasSubmitted = true,
                score = updatedScore,
                submittedAnswers =
                    currentState.submittedAnswers +
                            (question.id to selectedAnswer),
            )
        }
    }

    fun moveToNextQuestion() {
        _uiState.update { currentState ->
            if (!currentState.hasSubmitted) {
                return@update currentState
            }

            if (currentState.isLastQuestion) {
                currentState.copy(
                    isComplete = true,
                )
            } else {
                currentState.copy(
                    currentQuestionIndex =
                        currentState.currentQuestionIndex + 1,
                    selectedAnswer = null,
                    hasSubmitted = false,
                )
            }
        }
    }

    fun restartQuiz() {
        _uiState.update { currentState ->
            currentState.copy(
                currentQuestionIndex = 0,
                selectedAnswer = null,
                hasSubmitted = false,
                score = 0,
                isComplete = false,
            )
        }
    }
}