package com.example.dinopath.domain.repository

import com.example.dinopath.domain.model.DinosaurSpecimen
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun observeFavourites(): Flow<List<DinosaurSpecimen>>

    fun observeIsFavourite(
        specimenId: String,
    ): Flow<Boolean>

    suspend fun addFavourite(
        specimen: DinosaurSpecimen,
    )

    suspend fun removeFavourite(
        specimenId: String,
    )
}