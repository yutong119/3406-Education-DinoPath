package com.example.dinopath.ui.knowledge

import com.example.dinopath.domain.model.QuizQuestion
import com.example.dinopath.domain.repository.QuizRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KnowledgeCheckViewModelTest {

    private lateinit var viewModel: KnowledgeCheckViewModel

    @Before
    fun setUp() {
        viewModel = KnowledgeCheckViewModel(
            quizRepository = FakeQuizRepository(),
        )
    }

    @Test
    fun initialState_containsQuestionsAndStartsAtFirstQuestion() {
        val state = viewModel.uiState.value

        assertEquals(3, state.totalQuestions)
        assertEquals(0, state.currentQuestionIndex)
        assertEquals(0, state.score)
        assertFalse(state.hasSubmitted)
        assertFalse(state.isComplete)
    }

    @Test
    fun submitCorrectAnswer_increasesScore() {
        viewModel.selectAnswer("Correct A")
        viewModel.submitAnswer()

        val state = viewModel.uiState.value

        assertEquals(1, state.score)
        assertTrue(state.hasSubmitted)
    }

    @Test
    fun submitIncorrectAnswer_doesNotIncreaseScore() {
        viewModel.selectAnswer("Wrong")
        viewModel.submitAnswer()

        val state = viewModel.uiState.value

        assertEquals(0, state.score)
        assertTrue(state.hasSubmitted)
    }

    @Test
    fun moveToNextQuestion_resetsSelectionAndSubmission() {
        viewModel.selectAnswer("Correct A")
        viewModel.submitAnswer()
        viewModel.moveToNextQuestion()

        val state = viewModel.uiState.value

        assertEquals(1, state.currentQuestionIndex)
        assertEquals(null, state.selectedAnswer)
        assertFalse(state.hasSubmitted)
    }

    @Test
    fun finishingLastQuestion_marksQuizComplete() {
        repeat(3) {
            viewModel.selectAnswer(
                when (it) {
                    0 -> "Correct A"
                    1 -> "Correct B"
                    else -> "Correct C"
                },
            )
            viewModel.submitAnswer()
            viewModel.moveToNextQuestion()
        }

        assertTrue(viewModel.uiState.value.isComplete)
        assertEquals(3, viewModel.uiState.value.score)
    }
}

private class FakeQuizRepository : QuizRepository {

    override fun getJurassicQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 1,
                question = "Question A",
                options = listOf("Correct A", "Wrong"),
                correctAnswer = "Correct A",
                explanation = "Explanation A",
            ),
            QuizQuestion(
                id = 2,
                question = "Question B",
                options = listOf("Correct B", "Wrong"),
                correctAnswer = "Correct B",
                explanation = "Explanation B",
            ),
            QuizQuestion(
                id = 3,
                question = "Question C",
                options = listOf("Correct C", "Wrong"),
                correctAnswer = "Correct C",
                explanation = "Explanation C",
            ),
        )
    }
}