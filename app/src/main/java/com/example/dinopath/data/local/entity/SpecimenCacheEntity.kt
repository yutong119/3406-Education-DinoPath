package com.example.dinopath.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "specimen_cache")
data class SpecimenCacheEntity(
    @PrimaryKey
    val queryTitle: String,
    val displayTitle: String,
    val summary: String,
    val imageUrl: String?,
    val cachedAt: Long = System.currentTimeMillis(),
)