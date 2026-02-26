package com.example.myapplication

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.BishkekDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyCityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        MyCityUiState(categories = BishkekDataProvider.categories)
    )
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    fun onCategorySelected(id: Int) {
        _uiState.update { state ->
            state.copy(
                selectedCategoryId = id,
                placesBySelectedCategory = BishkekDataProvider.getPlacesByCategory(id),
                selectedPlaceId = null,
                selectedPlace = null
            )
        }
    }

    fun onPlaceSelected(id: Int) {
        _uiState.update { state ->
            state.copy(
                selectedPlaceId = id,
                selectedPlace = BishkekDataProvider.getPlace(id)
            )
        }
    }

    fun onBackFromDetails() {
        _uiState.update { state ->
            state.copy(
                selectedPlaceId = null,
                selectedPlace = null
            )
        }
    }

    fun onBackFromPlaces() {
        _uiState.update { state ->
            state.copy(
                selectedCategoryId = null,
                placesBySelectedCategory = emptyList(),
                selectedPlaceId = null,
                selectedPlace = null
            )
        }
    }
}
