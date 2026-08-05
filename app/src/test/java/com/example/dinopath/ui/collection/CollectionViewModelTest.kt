package com.example.dinopath.ui.collection

import com.example.dinopath.domain.model.DinosaurSpecimen
import com.example.dinopath.domain.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
class CollectionViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: CollectionViewModel
    private lateinit var repository: FakeCollectionRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeCollectionRepository()
        viewModel = CollectionViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_loadsFavourites() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        val specimen = DinosaurSpecimen("1", "Dino", "Period", "Diet", "Desc")
        repository.addFavourite(specimen)

        val state = viewModel.uiState.value
        assertEquals(1, state.favourites.size)
        assertEquals("1", state.favourites.first().id)
    }

    @Test
    fun removeFavourite_updatesStateAndRepository() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        val specimen = DinosaurSpecimen("1", "Dino", "Period", "Diet", "Desc")
        repository.addFavourite(specimen)

        viewModel.removeFavourite("1")

        assertTrue(repository.favourites.isEmpty())
        assertTrue(viewModel.uiState.value.favourites.isEmpty())
        assertNull(viewModel.uiState.value.removingSpecimenId)
    }

    @Test
    fun removeFavourite_failure_setsErrorMessage() = runTest {
        val failingRepo = object : FakeCollectionRepository() {
            override suspend fun removeFavourite(specimenId: String) {
                throw Exception("Delete failed")
            }
        }
        viewModel = CollectionViewModel(failingRepo)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }

        viewModel.removeFavourite("1")

        val state = viewModel.uiState.value
        assertEquals("Failed to remove specimen. Please try again.", state.errorMessage)
        assertNull(state.removingSpecimenId)
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
