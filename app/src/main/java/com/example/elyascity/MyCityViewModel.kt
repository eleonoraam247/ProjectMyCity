package com.example.elyascity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyCityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyCityUiState())
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    fun selectCategory(category: String) {
        val filteredPlaces = placesData.filter { it.category == category }
        _uiState.update {
            it.copy(
                selectedCategory = category,
                places = filteredPlaces,
                selectedPlace = filteredPlaces.firstOrNull()
            )
        }
    }

    fun selectPlace(place: Place) {
        _uiState.update {
            it.copy(selectedPlace = place)
        }
    }
}

data class MyCityUiState(
    val selectedCategory: String = "",
    val places: List<Place> = emptyList(),
    val selectedPlace: Place? = null
)

val placesData = listOf(
    // Cafes
    Place("Central Cafe", "Cafes", R.drawable.cafe),
    Place("Coffee Time", "Cafes", R.drawable.cafe),
    Place("Morning Brew", "Cafes", R.drawable.cafe),
    
    // Parks
    Place("Green Park", "Parks", R.drawable.cafe),
    Place("National Park", "Parks", R.drawable.cafe),
    Place("Sunset Garden", "Parks", R.drawable.cafe),
    
    // Museums
    Place("History Museum", "Museums", R.drawable.cafe),
    Place("Art Gallery", "Museums", R.drawable.cafe),
    Place("Science Center", "Museums", R.drawable.cafe),
    
    // Shopping
    Place("City Mall", "Shopping", R.drawable.cafe),
    Place("Old Market", "Shopping", R.drawable.cafe),
    Place("Fashion Street", "Shopping", R.drawable.cafe),
    
    // Restaurants
    Place("Italian Restaurant", "Restaurants", R.drawable.cafe),
    Place("Burger House", "Restaurants", R.drawable.cafe),
    Place("Sushi Bar", "Restaurants", R.drawable.cafe)
)
