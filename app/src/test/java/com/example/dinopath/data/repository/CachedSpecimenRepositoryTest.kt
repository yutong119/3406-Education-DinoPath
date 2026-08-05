package com.example.dinopath.data.repository

import com.example.dinopath.data.local.dao.DinoPathDao
import com.example.dinopath.data.local.entity.ChapterProgressEntity
import com.example.dinopath.data.local.entity.FavouriteSpecimenEntity
import com.example.dinopath.data.local.entity.MistakeEntity
import com.example.dinopath.data.local.entity.QuizResultEntity
import com.example.dinopath.data.local.entity.SpecimenCacheEntity
import com.example.dinopath.data.remote.WikipediaApiService
import com.example.dinopath.data.remote.dto.WikipediaPageSummaryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CachedSpecimenRepositoryTest {

    private val fakeDao = FakeDinoPathDao()
    private val fakeApi = FakeWikipediaApiService()
    private val repository = CachedSpecimenRepository(fakeApi, fakeDao)

    @Test
    fun `getSpecimenDetails returns failure for empty title`() = runTest {
        val result = repository.getSpecimenDetails("   ", false)
        assertTrue(result.isFailure)
    }

    @Test
    fun `getSpecimenDetails returns fresh cache without calling API`() = runTest {
        val title = "Stegosaurus"
        val cachedAt = System.currentTimeMillis()
        val cache = SpecimenCacheEntity(title, "Stegosaurus", "Summary", null, cachedAt)
        fakeDao.upsertSpecimenCache(cache)

        val result = repository.getSpecimenDetails(title, false)
        
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull()?.isFromCache)
        assertEquals(0, fakeApi.callCount)
    }

    @Test
    fun `getSpecimenDetails calls API and saves to cache when no cache exists`() = runTest {
        val title = "T-Rex"
        fakeApi.response = WikipediaPageSummaryDto(title, "T-Rex Summary", null)

        val result = repository.getSpecimenDetails(title, false)

        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrNull()?.isFromCache)
        assertEquals(1, fakeApi.callCount)
        assertEquals(title, fakeDao.getSpecimenCache(title)?.queryTitle)
    }

    @Test
    fun `getSpecimenDetails returns old cache when network fails`() = runTest {
        val title = "Triceratops"
        val oldCache = SpecimenCacheEntity(title, title, "Old Summary", null, 0L)
        fakeDao.upsertSpecimenCache(oldCache)
        fakeApi.shouldFail = true

        val result = repository.getSpecimenDetails(title, true)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull()?.isFromCache)
        assertEquals("Old Summary", result.getOrNull()?.summary)
    }

    @Test
    fun `getSpecimenDetails fails when network fails and no cache exists`() = runTest {
        val title = "Diplodocus"
        fakeApi.shouldFail = true

        val result = repository.getSpecimenDetails(title, false)

        assertTrue(result.isFailure)
    }

    private class FakeWikipediaApiService : WikipediaApiService {
        var response: WikipediaPageSummaryDto? = null
        var shouldFail = false
        var callCount = 0

        override suspend fun getPageSummary(title: String): WikipediaPageSummaryDto {
            callCount++
            if (shouldFail) throw Exception("Network error")
            return response ?: throw Exception("No response set")
        }
    }

    private class FakeDinoPathDao : DinoPathDao {
        private val cacheMap = mutableMapOf<String, SpecimenCacheEntity>()

        override suspend fun getSpecimenCache(queryTitle: String): SpecimenCacheEntity? = cacheMap[queryTitle]

        override suspend fun upsertSpecimenCache(cache: SpecimenCacheEntity) {
            cacheMap[cache.queryTitle] = cache
        }

        // Unused methods
        override fun observeChapterProgress(): Flow<List<ChapterProgressEntity>> = TODO()
        override suspend fun getChapterProgress(chapterId: Int): ChapterProgressEntity? = TODO()
        override suspend fun countChapters(): Int = TODO()
        override suspend fun upsertChapters(chapters: List<ChapterProgressEntity>) = TODO()
        override suspend fun updateChapterProgress(chapter: ChapterProgressEntity) = TODO()
        override suspend fun insertQuizResult(result: QuizResultEntity): Long = TODO()
        override suspend fun upsertMistake(mistake: MistakeEntity) = TODO()
        override suspend fun deleteMistake(chapterId: Int, questionId: Int) = TODO()
        override fun observeUnmasteredMistakes(): Flow<List<MistakeEntity>> = TODO()
        override suspend fun markMistakeMastered(chapterId: Int, questionId: Int, updatedAt: Long) = TODO()
        override fun observeQuizResults(): Flow<List<QuizResultEntity>> = TODO()
        override fun observeFavouriteSpecimens(): Flow<List<FavouriteSpecimenEntity>> = TODO()
        override fun observeIsFavourite(specimenId: String): Flow<Boolean> = TODO()
        override suspend fun upsertFavourite(specimen: FavouriteSpecimenEntity) = TODO()
        override suspend fun deleteFavourite(specimenId: String) = TODO()
        override suspend fun deleteExpiredSpecimenCache(expirationTime: Long) = TODO()
    }
}
