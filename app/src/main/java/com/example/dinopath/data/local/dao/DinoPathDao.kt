package com.example.dinopath.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.dinopath.data.local.entity.ChapterProgressEntity
import com.example.dinopath.data.local.entity.FavouriteSpecimenEntity
import com.example.dinopath.data.local.entity.MistakeEntity
import com.example.dinopath.data.local.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DinoPathDao {

    @Query(
        """
        SELECT * FROM chapter_progress
        ORDER BY chapterOrder ASC
        """
    )
    fun observeChapterProgress(): Flow<List<ChapterProgressEntity>>

    @Query(
        """
        SELECT * FROM chapter_progress
        WHERE chapterId = :chapterId
        LIMIT 1
        """
    )
    suspend fun getChapterProgress(
        chapterId: Int,
    ): ChapterProgressEntity?

    @Query("SELECT COUNT(*) FROM chapter_progress")
    suspend fun countChapters(): Int

    @Upsert
    suspend fun upsertChapters(
        chapters: List<ChapterProgressEntity>,
    )

    @Update
    suspend fun updateChapterProgress(
        chapter: ChapterProgressEntity,
    )

    @Insert
    suspend fun insertQuizResult(
        result: QuizResultEntity,
    ): Long

    @Upsert
    suspend fun upsertMistake(
        mistake: MistakeEntity,
    )

    @Query(
        """
        DELETE FROM mistakes
        WHERE chapterId = :chapterId
        AND questionId = :questionId
        """
    )
    suspend fun deleteMistake(
        chapterId: Int,
        questionId: Int,
    )

    @Query(
        """
        SELECT * FROM mistakes
        WHERE isMastered = 0
        ORDER BY updatedAt DESC
        """
    )
    fun observeUnmasteredMistakes(): Flow<List<MistakeEntity>>

    @Query(
        """
        SELECT * FROM quiz_results
        ORDER BY completedAt DESC
        """
    )
    fun observeQuizResults(): Flow<List<QuizResultEntity>>

    @Query(
        """
        UPDATE mistakes
        SET isMastered = 1,
            updatedAt = :updatedAt
        WHERE chapterId = :chapterId
        AND questionId = :questionId
        """
    )
    suspend fun markMistakeMastered(
        chapterId: Int,
        questionId: Int,
        updatedAt: Long = System.currentTimeMillis(),
    )

    @Query(
        """
    SELECT * FROM favourite_specimens
    ORDER BY savedAt DESC
    """
    )
    fun observeFavouriteSpecimens():
            Flow<List<FavouriteSpecimenEntity>>

    @Upsert
    suspend fun upsertFavourite(
        specimen: FavouriteSpecimenEntity,
    )

    @Query(
        """
    DELETE FROM favourite_specimens
    WHERE specimenId = :specimenId
    """
    )
    suspend fun deleteFavourite(
        specimenId: String,
    )
}