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
    Place(
        "Kulikov", 
        "Cafes", 
        R.drawable.kulikov,
        "A famous pastry shop known for its delicious cakes and cozy atmosphere."
    ),
    Place(
        "Makaronnaya", 
        "Cafes", 
        R.drawable.cats,
        "The best place for pasta lovers with a wide variety of authentic Italian recipes."
    ),
    Place(
        "Utrechkovv and cats", 
        "Cafes", 
        R.drawable.makaron,
        "A unique cat cafe where you can enjoy your coffee in the company of friendly cats."
    ),
    
    // Parks
    Place(
        "Yntymak Park", 
        "Parks", 
        R.drawable.yntymak,
        "A modern park with great walking paths, playgrounds, and a beautiful fountain."
    ),
    Place(
        "Panfilova Park", 
        "Parks", 
        R.drawable.panfilov,
        "The oldest park in the city, famous for its amusement rides and historical monuments."
    ),
    Place(
        "Evrazia", 
        "Parks", 
        R.drawable.evrazia,
        "A quiet green area perfect for family picnics and evening walks."
    ),

    // Museums
    Place(
        "Museum named by Aitiev Gaparov", 
        "Museums", 
        R.drawable.agmuseum,
        "The National Museum of Fine Arts, featuring a rich collection of Kyrgyz national art."
    ),
    Place(
        "National Kyrgyzstan Museum", 
        "Museums", 
        R.drawable.krmuseum,
        "Explore the history of Kyrgyzstan from ancient times to the present day."
    ),
    Place(
        "Museum named by Suimonkulov Chokmorova", 
        "Museums", 
        R.drawable.schmuseum,
        "Dedicated to the life and work of the famous Kyrgyz actor and artist Suimonkul Chokmorov."
    ),
    
    // Shopping
    Place(
        "Asia Mall", 
        "Shopping", 
        R.drawable.asiamall,
        "A large shopping center with many international brands, a cinema, and a food court."
    ),
    Place(
        "Bishkek Park", 
        "Shopping", 
        R.drawable.biskpark,
        "A trendy shopping mall in the city center with a rooftop terrace and ice rink."
    ),
    Place(
        "TSUM", 
        "Shopping", 
        R.drawable.tsum,
        "The central department store, a great place to buy souvenirs and electronics."
    ),
    
    // Restaurants
    Place(
        "Embassy", 
        "Restaurants", 
        R.drawable.embassy,
        "An upscale restaurant offering a fine dining experience with international cuisine."
    ),
    Place(
        "Gangnam", 
        "Restaurants", 
        R.drawable.gangnam,
        "Authentic Korean BBQ restaurant with a lively atmosphere and great service."
    ),
    Place(
        "Taller Gastrobisto", 
        "Restaurants", 
        R.drawable.tallergastrobisto,
        "A modern bistro focusing on local ingredients and creative culinary techniques."
    ),

    // Bars
    Place(
        "No Name Bar", 
        "Bars", 
        R.drawable.cafe,
        "A hidden gem with great cocktails and an underground vibe."
    ),
    Place(
        "Abstract", 
        "Bars", 
        R.drawable.cafe,
        "A stylish bar with modern art on the walls and a wide selection of drinks."
    ),
    Place(
        "Kevins", 
        "Bars", 
        R.drawable.cafe,
        "A popular sports bar where you can watch matches and enjoy cold beer."
    )
)
