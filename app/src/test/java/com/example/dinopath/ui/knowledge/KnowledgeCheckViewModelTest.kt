package com.example.dinopath.ui.knowledge

import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.MistakeSummary
import com.example.dinopath.domain.model.QuizCompletion
import com.example.dinopath.domain.model.QuizHistory
import com.example.dinopath.domain.model.QuizQuestion
import com.example.dinopath.domain.repository.LearningProgressRepository
import com.example.dinopath.domain.repository.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KnowledgeCheckViewModelTest {

    private val testDispatcher =
        UnconfinedTestDispatcher()

    private lateinit var viewModel: KnowledgeCheckViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = KnowledgeCheckViewModel(
            quizRepository = FakeQuizRepository(),
            learningProgressRepository =
                FakeLearningProgressRepository(),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
    fun finishingLastQuestion_marksQuizComplete() = runTest {
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

        val state = viewModel.uiState.value

        assertTrue(state.isComplete)
        assertEquals(3, state.score)
        assertEquals(3, state.earnedStars)
        assertEquals(100, state.savedAccuracy)
    }

    @Test
    fun restartQuiz_resetsAllState() = runTest {
        // Complete quiz first
        repeat(3) {
            viewModel.selectAnswer("Correct A")
            viewModel.submitAnswer()
            viewModel.moveToNextQuestion()
        }

        viewModel.restartQuiz()
        val state = viewModel.uiState.value

        assertEquals(0, state.currentQuestionIndex)
        assertFalse(state.isComplete)
        assertEquals(0, state.score)
        assertEquals(emptyMap<Int, String>(), state.submittedAnswers)
    }

    @Test
    fun savingFailure_showsError() = runTest {
        val failingRepo = object : FakeLearningProgressRepository() {
            override suspend fun saveQuizCompletion(
                chapterId: Int,
                questions: List<QuizQuestion>,
                answers: Map<Int, String>,
                score: Int,
            ): QuizCompletion {
                throw Exception("Network error")
            }
        }
        viewModel = KnowledgeCheckViewModel(
            quizRepository = FakeQuizRepository(),
            learningProgressRepository = failingRepo,
        )

        repeat(3) {
            viewModel.selectAnswer("Correct A")
            viewModel.submitAnswer()
            viewModel.moveToNextQuestion()
        }

        val state = viewModel.uiState.value
        assertFalse(state.isComplete)
        assertEquals("Network error", state.saveError)
    }

    @Test
    fun multipleClicksOnLastQuestion_doesNotTriggerMultipleSaves() = runTest {
        var callCount = 0
        val countingRepo = object : FakeLearningProgressRepository() {
            override suspend fun saveQuizCompletion(
                chapterId: Int,
                questions: List<QuizQuestion>,
                answers: Map<Int, String>,
                score: Int,
            ): QuizCompletion {
                callCount++
                return super.saveQuizCompletion(chapterId, questions, answers, score)
            }
        }
        viewModel = KnowledgeCheckViewModel(
            quizRepository = FakeQuizRepository(),
            learningProgressRepository = countingRepo,
        )

        // Complete first 2 questions
        repeat(2) {
            viewModel.selectAnswer("Correct A")
            viewModel.submitAnswer()
            viewModel.moveToNextQuestion()
        }

        // Last question
        viewModel.selectAnswer("Correct A")
        viewModel.submitAnswer()
        
        // Trigger move to next (which triggers save) multiple times
        viewModel.moveToNextQuestion()
        viewModel.moveToNextQuestion()
        viewModel.moveToNextQuestion()

        assertEquals(1, callCount)
    }
}

private class FakeQuizRepository : QuizRepository {

    override fun getJurassicQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 1,
                question = "Question A",
                options = listOf(
                    "Correct A",
                    "Wrong",
                ),
                correctAnswer = "Correct A",
                explanation = "Explanation A",
            ),
            QuizQuestion(
                id = 2,
                question = "Question B",
                options = listOf(
                    "Correct B",
                    "Wrong",
                ),
                correctAnswer = "Correct B",
                explanation = "Explanation B",
            ),
            QuizQuestion(
                id = 3,
                question = "Question C",
                options = listOf(
                    "Correct C",
                    "Wrong",
                ),
                correctAnswer = "Correct C",
                explanation = "Explanation C",
            ),
        )
    }
}

private open class FakeLearningProgressRepository :
    LearningProgressRepository {

    override fun observeChapterProgress():
            Flow<List<ChapterProgress>> {
        return flowOf(emptyList())
    }

    override fun observeQuizHistory(): Flow<List<QuizHistory>> {
        return flowOf(emptyList())
    }

    override fun observeUnmasteredMistakes(): Flow<List<MistakeSummary>> {
        return flowOf(emptyList())
    }

    override suspend fun ensureInitialChapters() {
        // No database seeding is required in this unit test.
    }

    override suspend fun markMistakeMastered(
        chapterId: Int,
        questionId: Int,
    ) {
        // Unused in these tests
    }

    override suspend fun saveQuizCompletion(
        chapterId: Int,
        questions: List<QuizQuestion>,
        answers: Map<Int, String>,
        score: Int,
    ): QuizCompletion {
        val totalQuestions = questions.size

        val accuracy =
            if (totalQuestions == 0) {
                0
            } else {
                score * 100 / totalQuestions
            }

        val stars =
            when {
                totalQuestions == 0 -> 0
                score == totalQuestions -> 3
                score == totalQuestions - 1 -> 2
                else -> 1
            }

        return QuizCompletion(
            chapterId = chapterId,
            score = score,
            totalQuestions = totalQuestions,
            accuracy = accuracy,
            stars = stars,
        )
    }
}