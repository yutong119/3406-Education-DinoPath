package com.example.dinopath.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val resultId: Long = 0,
    val chapterId: Int,
    val score: Int,
    val totalQuestions: Int,
    val accuracy: Int,
    val stars: Int,
    val completedAt: Long = System.currentTimeMillis(),
)