package com.example.dinopath.domain.repository

import com.example.dinopath.domain.model.SpecimenDetails

interface SpecimenRepository {

    suspend fun getSpecimenDetails(
        queryTitle: String,
        forceRefresh: Boolean = false,
    ): Result<SpecimenDetails>
}