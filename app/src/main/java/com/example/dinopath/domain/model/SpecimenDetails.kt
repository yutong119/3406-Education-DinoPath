package com.example.dinopath.domain.model

data class SpecimenDetails(
    val queryTitle: String,
    val displayTitle: String,
    val summary: String,
    val imageUrl: String?,
    val isFromCache: Boolean,
)