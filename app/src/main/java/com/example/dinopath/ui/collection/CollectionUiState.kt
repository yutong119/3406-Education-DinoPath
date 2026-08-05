package com.example.dinopath.ui.collection

import com.example.dinopath.domain.model.DinosaurSpecimen

data class CollectionUiState(
    val favourites: List<DinosaurSpecimen> = emptyList(),
    val isLoading: Boolean = true,
)
