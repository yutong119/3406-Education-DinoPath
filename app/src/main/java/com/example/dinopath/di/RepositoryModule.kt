package com.example.dinopath.di

import com.example.dinopath.data.repository.StaticQuizRepository
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
}