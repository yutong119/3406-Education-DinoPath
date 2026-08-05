package com.example.dinopath.domain.model

data class DinosaurSpecimen(
    val id: String,
    val name: String,
    val period: String,
    val diet: String,
    val description: String,
    val isFavourite: Boolean = false,
)
