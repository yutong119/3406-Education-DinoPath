package com.example.dinopath.data.repository

import com.example.dinopath.domain.repository.CollectionRepository
import com.example.dinopath.domain.repository.LearningProgressRepository
import com.example.dinopath.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.dinopath.data.preferences.DataStoreUserPreferencesRepository
import com.example.dinopath.domain.repository.UserPreferencesRepository
import com.example.dinopath.domain.repository.SpecimenRepository

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

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(
        implementation: LocalCollectionRepository,
    ): CollectionRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        implementation:
        DataStoreUserPreferencesRepository,
    ): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindSpecimenRepository(
        implementation: CachedSpecimenRepository,
    ): SpecimenRepository

}