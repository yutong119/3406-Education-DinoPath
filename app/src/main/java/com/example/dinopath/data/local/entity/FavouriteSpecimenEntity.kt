package com.example.dinopath.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_specimens")
data class FavouriteSpecimenEntity(
    @PrimaryKey
    val specimenId: String,
    val name: String,
    val period: String,
    val diet: String,
    val description: String,
    val savedAt: Long = System.currentTimeMillis(),
)