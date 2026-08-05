package com.example.dinopath.data.repository

import com.example.dinopath.data.local.dao.DinoPathDao
import com.example.dinopath.data.local.entity.FavouriteSpecimenEntity
import com.example.dinopath.domain.model.DinosaurSpecimen
import com.example.dinopath.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCollectionRepository @Inject constructor(
    private val dao: DinoPathDao,
) : CollectionRepository {

    override fun observeFavourites(): Flow<List<DinosaurSpecimen>> {
        return dao.observeFavouriteSpecimens().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addFavourite(specimen: DinosaurSpecimen) {
        dao.upsertFavourite(specimen.toEntity())
    }

    override suspend fun removeFavourite(specimenId: String) {
        dao.deleteFavourite(specimenId)
    }
}

private fun FavouriteSpecimenEntity.toDomain(): DinosaurSpecimen {
    return DinosaurSpecimen(
        id = specimenId,
        name = name,
        period = period,
        diet = diet,
        description = description,
        isFavourite = true,
    )
}

private fun DinosaurSpecimen.toEntity(): FavouriteSpecimenEntity {
    return FavouriteSpecimenEntity(
        specimenId = id,
        name = name,
        period = period,
        diet = diet,
        description = description,
    )
}
