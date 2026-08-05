package com.example.dinopath.domain.repository

import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.QuizCompletion
import com.example.dinopath.domain.model.QuizQuestion
import kotlinx.coroutines.flow.Flow
import com.example.dinopath.domain.model.MistakeSummary
import com.example.dinopath.domain.model.QuizHistory

interface LearningProgressRepository {

    fun observeChapterProgress(): Flow<List<ChapterProgress>>

    fun observeQuizHistory(): Flow<List<QuizHistory>>

    fun observeUnmasteredMistakes(): Flow<List<MistakeSummary>>

    suspend fun ensureInitialChapters()

    suspend fun saveQuizCompletion(
        chapterId: Int,
        questions: List<QuizQuestion>,
        answers: Map<Int, String>,
        score: Int,
    ): QuizCompletion

    suspend fun markMistakeMastered(
        chapterId: Int,
        questionId: Int,
    )
}
