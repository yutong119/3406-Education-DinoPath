package com.example.dinopath.data.repository

import com.example.dinopath.domain.repository.LearningProgressRepository
import com.example.dinopath.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuizRepository(
        implementation: StaticQuizRepository,
    ): QuizRepository

    @Binds
    @Singleton
    abstract fun bindLearningProgressRepository(
        implementation: LocalLearningProgressRepository,
    ): LearningProgressRepository
}