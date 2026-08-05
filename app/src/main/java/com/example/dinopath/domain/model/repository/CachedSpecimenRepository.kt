package com.example.dinopath.data.repository

import com.example.dinopath.data.local.dao.DinoPathDao
import com.example.dinopath.data.local.entity.SpecimenCacheEntity
import com.example.dinopath.data.remote.WikipediaApiService
import com.example.dinopath.domain.model.SpecimenDetails
import com.example.dinopath.domain.repository.SpecimenRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val CACHE_VALIDITY_MILLIS =
    24L * 60L * 60L * 1_000L

@Singleton
class CachedSpecimenRepository @Inject constructor(
    private val apiService: WikipediaApiService,
    private val dao: DinoPathDao,
) : SpecimenRepository {

    override suspend fun getSpecimenDetails(
        queryTitle: String,
        forceRefresh: Boolean,
    ): Result<SpecimenDetails> {
        val normalizedTitle = queryTitle.trim()

        if (normalizedTitle.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Specimen title cannot be empty.",
                ),
            )
        }

        val cachedEntity =
            dao.getSpecimenCache(normalizedTitle)

        val isCacheFresh =
            cachedEntity != null &&
                    System.currentTimeMillis() -
                    cachedEntity.cachedAt <
                    CACHE_VALIDITY_MILLIS

        if (!forceRefresh && isCacheFresh) {
            return Result.success(
                cachedEntity.toDomain(
                    isFromCache = true,
                ),
            )
        }

        return runCatching {
            val response =
                apiService.getPageSummary(
                    title = normalizedTitle,
                )

            val summary =
                response.extract
                    ?.trim()
                    .orEmpty()

            if (summary.isBlank()) {
                error(
                    "Wikipedia did not return a summary.",
                )
            }

            val cacheEntity =
                SpecimenCacheEntity(
                    queryTitle = normalizedTitle,
                    displayTitle =
                        response.title.ifBlank {
                            normalizedTitle
                        },
                    summary = summary,
                    imageUrl =
                        response.thumbnail?.source,
                    cachedAt =
                        System.currentTimeMillis(),
                )

            dao.upsertSpecimenCache(cacheEntity)

            cacheEntity.toDomain(
                isFromCache = false,
            )
        }.recoverCatching { networkError ->
            cachedEntity?.toDomain(
                isFromCache = true,
            ) ?: throw networkError
        }
    }
}

private fun SpecimenCacheEntity.toDomain(
    isFromCache: Boolean,
): SpecimenDetails {
    return SpecimenDetails(
        queryTitle = queryTitle,
        displayTitle = displayTitle,
        summary = summary,
        imageUrl = imageUrl,
        isFromCache = isFromCache,
    )
}