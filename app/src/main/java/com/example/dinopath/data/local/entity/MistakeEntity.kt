package com.example.dinopath.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "mistakes",
    primaryKeys = ["chapterId", "questionId"],
)
data class MistakeEntity(
    val chapterId: Int,
    val questionId: Int,
    val question: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val explanation: String,
    val isMastered: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
)