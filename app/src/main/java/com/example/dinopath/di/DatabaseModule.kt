package com.example.dinopath.di

import android.content.Context
import androidx.room.Room
import com.example.dinopath.data.local.DinoPathDatabase
import com.example.dinopath.data.local.MIGRATION_1_2
import com.example.dinopath.data.local.dao.DinoPathDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDinoPathDatabase(
        @ApplicationContext context: Context,
    ): DinoPathDatabase {
        return Room.databaseBuilder(
            context,
            DinoPathDatabase::class.java,
            "dinopath_database",
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideDinoPathDao(
        database: DinoPathDatabase,
    ): DinoPathDao {
        return database.dinoPathDao()
    }
}
