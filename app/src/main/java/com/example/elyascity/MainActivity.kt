package com.example.elyascity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.elyascity.ui.theme.*

// --- Data Models ---
data class CityCategory(val title: String, val icon: ImageVector)
data class Place(val name: String, val category: String, val imageRes: Int, val description: String)

val categories = listOf(
    CityCategory("Restaurants", Icons.Default.Restaurant),
    CityCategory("Parks", Icons.Default.Park),
    CityCategory("Museums", Icons.Default.Museum),
    CityCategory("Shopping", Icons.Default.ShoppingBag),
    CityCategory("Cafes", Icons.Default.LocalCafe),
    CityCategory("Bars", Icons.Default.Nightlife)
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElyasCityTheme {
                val windowSize = calculateWindowSizeClass(this)
                val isTablet = windowSize.widthSizeClass != WindowWidthSizeClass.Compact
                val navController = rememberNavController()
                val viewModel: MyCityViewModel = viewModel()

                MyCityApp(
                    isTablet = isTablet,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun MyCityApp(
    isTablet: Boolean,
    navController: NavHostController,
    viewModel: MyCityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Row(modifier = Modifier.fillMaxSize()) {
        if (isTablet) {
            CityNavigationRail(
                navController = navController,
                selectedCategory = uiState.selectedCategory,
                onCategoryClick = { 
                    viewModel.selectCategory(it)
                    navController.navigate("places") {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        isTablet = isTablet,
                        onCategoryClick = { 
                            viewModel.selectCategory(it)
                            navController.navigate("places")
                        }
                    )
                }
                composable("places") {
                    PlacesScreen(
                        isTablet = isTablet,
                        uiState = uiState,
                        onPlaceClick = { 
                            viewModel.selectPlace(it)
                            if (!isTablet) navController.navigate("details")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("details") {
                    DetailsScreen(
                        place = uiState.selectedPlace,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun CityNavigationRail(
    navController: NavHostController,
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationRail(containerColor = BluePrimary) {
        NavigationRailItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = BluePrimary,
                indicatorColor = Color.White,
                unselectedIconColor = Color.White.copy(0.7f),
                selectedTextColor = Color.White,
                unselectedTextColor = Color.White.copy(0.7f)
            )
        )
        categories.forEach { category ->
            NavigationRailItem(
                selected = currentRoute == "places" && selectedCategory == category.title,
                onClick = { onCategoryClick(category.title) },
                icon = { Icon(category.icon, null) },
                label = { Text(category.title) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = BluePrimary,
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.White.copy(0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(0.7f)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(isTablet: Boolean, onCategoryClick: (String) -> Unit) {
    Scaffold(
        topBar = {
            if (!isTablet) {
                TopAppBar(
                    title = { Text("My City", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BluePrimary,
                        titleContentColor = Color.White
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BlueBackground)
                .then(if (isTablet) Modifier.padding(32.dp) else Modifier.padding(16.dp))
        ) {
            Text(
                text = if (isTablet) "Welcome to Elyas City" else "Select a Category",
                style = if (isTablet) MaterialTheme.typography.displayMedium else MaterialTheme.typography.headlineSmall,
                color = BlueAccent,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (isTablet) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(250.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(categories) { category ->
                        CategoryCard(category, onCategoryClick)
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    categories.forEach { category ->
                        CategoryButton(category, onCategoryClick)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: CityCategory, onClick: (String) -> Unit) {
    Card(
        onClick = { onClick(category.title) },
        modifier = Modifier.height(180.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(BluePrimary, BlueSoft))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(category.icon, null, modifier = Modifier.size(40.dp), tint = Color.White)
                Spacer(Modifier.height(12.dp))
                Text(category.title, style = MaterialTheme.typography.titleLarge, color = Color.White)
            }
        }
    }
}

@Composable
fun CategoryButton(category: CityCategory, onClick: (String) -> Unit) {
    Card(
        onClick = { onClick(category.title) },
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(category.icon, null, tint = BluePrimary, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Text(
                category.title,
                style = MaterialTheme.typography.titleMedium,
                color = BlueAccent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    isTablet: Boolean,
    uiState: MyCityUiState,
    onPlaceClick: (Place) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            if (!isTablet) {
                TopAppBar(
                    title = { Text(uiState.selectedCategory) },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BluePrimary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        }
    ) { padding ->
        if (isTablet) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
            ) {
                LazyColumn(modifier = Modifier.weight(1f).padding(end = 24.dp)) {
                    item {
                        Text(
                            uiState.selectedCategory,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                    items(uiState.places) { place ->
                        PlaceItem(place, isSelected = uiState.selectedPlace == place, onClick = onPlaceClick)
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    uiState.selectedPlace?.let { PlaceDetailContent(it) }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(uiState.places) { place ->
                    PlaceItem(place, isSelected = false, onClick = onPlaceClick, modifier = Modifier.width(320.dp))
                }
            }
        }
    }
}

@Composable
fun PlaceItem(place: Place, isSelected: Boolean, onClick: (Place) -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = { onClick(place) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) BlueSoft else Color.White),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(place.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                place.name,
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) BluePrimary else BlueAccent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(place: Place?, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        place?.let { PlaceDetailContent(it, Modifier.padding(padding)) }
    }
}

@Composable
fun PlaceDetailContent(place: Place, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Image(
            painter = painterResource(place.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(32.dp)) {
            Text(
                place.name,
                style = MaterialTheme.typography.displayMedium,
                color = BlueAccent,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
