package com.example.myapplication

import com.example.myapplication.data.model.Category
import com.example.myapplication.data.model.Place

data class MyCityUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val placesBySelectedCategory: List<Place> = emptyList(),
    val selectedPlaceId: Int? = null,
    val selectedPlace: Place? = null
)
