package com.example.dinopath.domain.repository

import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.QuizCompletion
import com.example.dinopath.domain.model.QuizQuestion
import kotlinx.coroutines.flow.Flow

interface LearningProgressRepository {

    fun observeChapterProgress(): Flow<List<ChapterProgress>>

    suspend fun ensureInitialChapters()

    suspend fun saveQuizCompletion(
        chapterId: Int,
        questions: List<QuizQuestion>,
        answers: Map<Int, String>,
        score: Int,
    ): QuizCompletion
}