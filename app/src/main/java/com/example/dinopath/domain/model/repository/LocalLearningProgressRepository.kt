package com.example.dinopath.data.repository

import androidx.room.withTransaction
import com.example.dinopath.data.local.DinoPathDatabase
import com.example.dinopath.data.local.dao.DinoPathDao
import com.example.dinopath.data.local.entity.ChapterProgressEntity
import com.example.dinopath.data.local.entity.MistakeEntity
import com.example.dinopath.data.local.entity.QuizResultEntity
import com.example.dinopath.domain.model.ChapterIds
import com.example.dinopath.domain.model.ChapterProgress
import com.example.dinopath.domain.model.ChapterStatus
import com.example.dinopath.domain.model.QuizCompletion
import com.example.dinopath.domain.model.QuizQuestion
import com.example.dinopath.domain.repository.LearningProgressRepository
import com.example.dinopath.domain.scoring.calculateAccuracy
import com.example.dinopath.domain.scoring.calculateStars
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import com.example.dinopath.domain.model.MistakeSummary
import com.example.dinopath.domain.model.QuizHistory

@Singleton
class LocalLearningProgressRepository @Inject constructor(
    private val database: DinoPathDatabase,
    private val dao: DinoPathDao,
) : LearningProgressRepository {

    override fun observeChapterProgress(): Flow<List<ChapterProgress>> {
        return dao.observeChapterProgress().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun observeQuizHistory(): Flow<List<QuizHistory>> {
        return dao.observeQuizResults().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun observeUnmasteredMistakes():
            Flow<List<MistakeSummary>> {
        return dao.observeUnmasteredMistakes().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun ensureInitialChapters() {
        if (dao.countChapters() > 0) {
            return
        }

        dao.upsertChapters(initialChapters)
    }

    override suspend fun markMistakeMastered(
        chapterId: Int,
        questionId: Int,
    ) {
        dao.markMistakeMastered(
            chapterId = chapterId,
            questionId = questionId,
            updatedAt = System.currentTimeMillis(),
        )
    }

    override suspend fun saveQuizCompletion(
        chapterId: Int,
        questions: List<QuizQuestion>,
        answers: Map<Int, String>,
        score: Int,
    ): QuizCompletion {
        val totalQuestions = questions.size
        val accuracy = calculateAccuracy(
            score = score,
            totalQuestions = totalQuestions,
        )

        val stars = calculateStars(
            score = score,
            totalQuestions = totalQuestions,
        )

        database.withTransaction {
            val currentChapter =
                dao.getChapterProgress(chapterId)
                    ?: error("Chapter $chapterId does not exist")

            dao.updateChapterProgress(
                currentChapter.copy(
                    status = ChapterStatus.COMPLETED.name,
                    isUnlocked = true,
                    stars = max(currentChapter.stars, stars),
                    bestScore = max(currentChapter.bestScore, score),
                    totalQuestions = max(
                        currentChapter.totalQuestions,
                        totalQuestions,
                    ),
                    bestAccuracy = max(
                        currentChapter.bestAccuracy,
                        accuracy,
                    ),
                    updatedAt = System.currentTimeMillis(),
                ),
            )

            dao.insertQuizResult(
                QuizResultEntity(
                    chapterId = chapterId,
                    score = score,
                    totalQuestions = totalQuestions,
                    accuracy = accuracy,
                    stars = stars,
                ),
            )

            questions.forEach { question ->
                val selectedAnswer = answers[question.id].orEmpty()
                val isCorrect =
                    selectedAnswer == question.correctAnswer

                if (isCorrect) {
                    dao.deleteMistake(
                        chapterId = chapterId,
                        questionId = question.id,
                    )
                } else {
                    dao.upsertMistake(
                        MistakeEntity(
                            chapterId = chapterId,
                            questionId = question.id,
                            question = question.question,
                            selectedAnswer = selectedAnswer,
                            correctAnswer = question.correctAnswer,
                            explanation = question.explanation,
                        ),
                    )
                }
            }

            val nextChapterId = chapterId + 1
            val nextChapter = dao.getChapterProgress(nextChapterId)

            if (
                nextChapter != null &&
                !nextChapter.isUnlocked
            ) {
                dao.updateChapterProgress(
                    nextChapter.copy(
                        status = ChapterStatus.IN_PROGRESS.name,
                        isUnlocked = true,
                        updatedAt = System.currentTimeMillis(),
                    ),
                )
            }
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

private fun ChapterProgressEntity.toDomain(): ChapterProgress {
    return ChapterProgress(
        chapterId = chapterId,
        chapterOrder = chapterOrder,
        title = title,
        subtitle = subtitle,
        status = ChapterStatus.valueOf(status),
        isUnlocked = isUnlocked,
        stars = stars,
        bestScore = bestScore,
        totalQuestions = totalQuestions,
        bestAccuracy = bestAccuracy,
    )
}

private fun QuizResultEntity.toDomain(): QuizHistory {
    return QuizHistory(
        resultId = resultId,
        chapterId = chapterId,
        score = score,
        totalQuestions = totalQuestions,
        accuracy = accuracy,
        stars = stars,
        completedAt = completedAt,
    )
}

private fun MistakeEntity.toDomain(): MistakeSummary {
    return MistakeSummary(
        chapterId = chapterId,
        questionId = questionId,
        question = question,
        selectedAnswer = selectedAnswer,
        correctAnswer = correctAnswer,
        explanation = explanation,
        isMastered = isMastered,
    )
}
private val initialChapters = listOf(
    ChapterProgressEntity(
        chapterId = ChapterIds.MEET_THE_DINOSAURS,
        chapterOrder = 1,
        title = "Meet the Dinosaurs",
        subtitle = "Introduction to prehistoric life",
        status = ChapterStatus.COMPLETED.name,
        isUnlocked = true,
        stars = 3,
        bestScore = 3,
        totalQuestions = 3,
        bestAccuracy = 100,
    ),
    ChapterProgressEntity(
        chapterId = ChapterIds.TRIASSIC_PERIOD,
        chapterOrder = 2,
        title = "Triassic Period",
        subtitle = "The first dinosaurs emerge",
        status = ChapterStatus.COMPLETED.name,
        isUnlocked = true,
        stars = 2,
        bestScore = 2,
        totalQuestions = 3,
        bestAccuracy = 66,
    ),
    ChapterProgressEntity(
        chapterId = ChapterIds.JURASSIC_PERIOD,
        chapterOrder = 3,
        title = "Jurassic Period",
        subtitle = "Giants dominate the Earth",
        status = ChapterStatus.IN_PROGRESS.name,
        isUnlocked = true,
    ),
    ChapterProgressEntity(
        chapterId = ChapterIds.CRETACEOUS_PERIOD,
        chapterOrder = 4,
        title = "Cretaceous Period",
        subtitle = "A changing prehistoric world",
        status = ChapterStatus.LOCKED.name,
        isUnlocked = false,
    ),
    ChapterProgressEntity(
        chapterId = ChapterIds.HABITATS_AND_DIETS,
        chapterOrder = 5,
        title = "Dinosaur Habitats and Diets",
        subtitle = "How dinosaurs lived and ate",
        status = ChapterStatus.LOCKED.name,
        isUnlocked = false,
    ),
    ChapterProgressEntity(
        chapterId = ChapterIds.MASS_EXTINCTION,
        chapterOrder = 6,
        title = "Mass Extinction",
        subtitle = "The end of the dinosaur age",
        status = ChapterStatus.LOCKED.name,
        isUnlocked = false,
    ),
    ChapterProgressEntity(
        chapterId = ChapterIds.MODERN_BIRDS,
        chapterOrder = 7,
        title = "Dinosaurs and Modern Birds",
        subtitle = "The dinosaurs that survived",
        status = ChapterStatus.LOCKED.name,
        isUnlocked = false,
    ),
)