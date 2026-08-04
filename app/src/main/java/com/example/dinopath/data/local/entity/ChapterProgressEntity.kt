package com.example.dinopath.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapter_progress")
data class ChapterProgressEntity(
    @PrimaryKey
    val chapterId: Int,
    val chapterOrder: Int,
    val title: String,
    val subtitle: String,
    val status: String,
    val isUnlocked: Boolean,
    val stars: Int = 0,
    val bestScore: Int = 0,
    val totalQuestions: Int = 0,
    val bestAccuracy: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
)