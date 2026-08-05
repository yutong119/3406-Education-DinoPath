package com.example.dinopath.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dinopath.data.local.dao.DinoPathDao
import com.example.dinopath.data.local.entity.ChapterProgressEntity
import com.example.dinopath.data.local.entity.FavouriteSpecimenEntity
import com.example.dinopath.data.local.entity.MistakeEntity
import com.example.dinopath.data.local.entity.QuizResultEntity
import com.example.dinopath.data.local.entity.SpecimenCacheEntity

@Database(
    entities = [
        ChapterProgressEntity::class,
        QuizResultEntity::class,
        MistakeEntity::class,
        FavouriteSpecimenEntity::class,
        SpecimenCacheEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class DinoPathDatabase : RoomDatabase() {
    abstract fun dinoPathDao(): DinoPathDao
}