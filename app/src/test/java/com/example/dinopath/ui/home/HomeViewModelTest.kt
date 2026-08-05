package com.example.dinopath.ui.home

import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.DinosaurSpecimen
import com.example.dinopath.domain.model.MistakeSummary
import com.example.dinopath.domain.model.QuizCompletion
import com.example.dinopath.domain.model.QuizHistory
import com.example.dinopath.domain.model.QuizQuestion
import com.example.dinopath.domain.repository.CollectionRepository
import com.example.dinopath.domain.repository.LearningProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: HomeViewModel
    private lateinit var learningRepository: FakeLearningProgressRepository
    private lateinit var collectionRepository: FakeCollectionRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        learningRepository = FakeLearningProgressRepository()
        collectionRepository = FakeCollectionRepository()

        viewModel = HomeViewModel(
            learningRepository = learningRepository,
            collectionRepository = collectionRepository,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isCorrect() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        val state = viewModel.uiState.value
        assertFalse(state.isFeaturedFavourite)
        assertFalse(state.isUpdatingFavourite)
        assertNull(state.favouriteError)
    }

    @Test
    fun toggleFavourite_addsWhenNotFavourite() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        viewModel.toggleFeaturedFavourite()

        assertTrue(viewModel.uiState.value.isFeaturedFavourite)
        assertEquals(1, collectionRepository.favourites.size)
        assertEquals(HomeViewModel.STEGOSAURUS.id, collectionRepository.favourites.first().id)
    }

    @Test
    fun toggleFavourite_removesWhenIsFavourite() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        // Add first
        viewModel.toggleFeaturedFavourite()
        assertTrue(viewModel.uiState.value.isFeaturedFavourite)

        // Toggle again to remove
        viewModel.toggleFeaturedFavourite()
        assertFalse(viewModel.uiState.value.isFeaturedFavourite)
        assertTrue(collectionRepository.favourites.isEmpty())
    }

    @Test
    fun toggleFavourite_failure_setsError() = runTest {
        val failingRepo = object : FakeCollectionRepository() {
            override suspend fun addFavourite(specimen: DinosaurSpecimen) {
                throw Exception("Database error")
            }
        }
        viewModel = HomeViewModel(
            learningRepository = learningRepository,
            collectionRepository = failingRepo,
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        viewModel.toggleFeaturedFavourite()

        val state = viewModel.uiState.value
        assertFalse(state.isUpdatingFavourite)
        assertEquals("Failed to update collection. Please try again.", state.favouriteError)
        assertFalse(state.isFeaturedFavourite)
    }
}

private open class FakeLearningProgressRepository : LearningProgressRepository {
    override fun observeChapterProgress(): Flow<List<ChapterProgress>> = flowOf(emptyList())
    override fun observeQuizHistory(): Flow<List<QuizHistory>> = flowOf(emptyList())
    override fun observeUnmasteredMistakes(): Flow<List<MistakeSummary>> = flowOf(emptyList())
    override suspend fun ensureInitialChapters() {}
    override suspend fun markMistakeMastered(chapterId: Int, questionId: Int) {}
    override suspend fun saveQuizCompletion(chapterId: Int, questions: List<QuizQuestion>, answers: Map<Int, String>, score: Int): QuizCompletion {
        return QuizCompletion(chapterId, score, questions.size, 0, 0)
    }
}

private open class FakeCollectionRepository : CollectionRepository {
    private val favouritesFlow = MutableStateFlow<List<DinosaurSpecimen>>(emptyList())
    val favourites get() = favouritesFlow.value

    override fun observeFavourites(): Flow<List<DinosaurSpecimen>> = favouritesFlow

    override fun observeIsFavourite(specimenId: String): Flow<Boolean> {
        return favouritesFlow.map { list -> list.any { it.id == specimenId } }
    }

    override suspend fun addFavourite(specimen: DinosaurSpecimen) {
        favouritesFlow.update { it + specimen }
    }

    override suspend fun removeFavourite(specimenId: String) {
        favouritesFlow.update { it.filterNot { item -> item.id == specimenId } }
    }
}
