package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.model.Category
import com.example.myapplication.data.model.Place
import com.example.myapplication.navigation.MyCityNavGraph
import com.example.myapplication.ui.components.CategoryCard
import com.example.myapplication.ui.components.PlaceCard
import android.content.Intent
import android.net.Uri

@Composable
fun MyCityApp(windowSizeClass: WindowSizeClass) {
    val viewModel: MyCityViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            val navController = rememberNavController()
            MyCityNavGraph(navController = navController, viewModel = viewModel)
        }
        WindowWidthSizeClass.Medium -> {
            MediumLayout(uiState = uiState, viewModel = viewModel)
        }
        else -> {
            ExpandedLayout(uiState = uiState, viewModel = viewModel)
        }
    }
}

// ─── Medium Layout: Categories (sidebar) + Places/Details (main area) ─────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediumLayout(
    uiState: MyCityUiState,
    viewModel: MyCityViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Бишкек — путеводитель") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Left panel: Categories
            CategoriesPanel(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategoryClick = { viewModel.onCategorySelected(it) },
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            VerticalDivider(thickness = 1.dp)

            // Right panel: Places list OR Details
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.selectedPlace != null -> {
                        DetailsPanel(
                            place = uiState.selectedPlace,
                            onBackClick = { viewModel.onBackFromDetails() },
                            showBackButton = true,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    uiState.selectedCategoryId != null -> {
                        PlacesPanel(
                            places = uiState.placesBySelectedCategory,
                            onPlaceClick = { viewModel.onPlaceSelected(it) },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        EmptyPanel(
                            message = "Выберите категорию слева",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

// ─── Expanded Layout: Categories | Places | Details ────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandedLayout(
    uiState: MyCityUiState,
    viewModel: MyCityViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Бишкек — путеводитель") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Left panel: Categories
            CategoriesPanel(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategoryClick = {
                    viewModel.onCategorySelected(it)
                    viewModel.onBackFromDetails()
                },
                modifier = Modifier
                    .width(260.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            VerticalDivider(thickness = 1.dp)

            // Middle panel: Places list
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            ) {
                if (uiState.selectedCategoryId != null) {
                    PlacesPanel(
                        places = uiState.placesBySelectedCategory,
                        onPlaceClick = { viewModel.onPlaceSelected(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    EmptyPanel(
                        message = "Выберите категорию",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            VerticalDivider(thickness = 1.dp)

            // Right panel: Details
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.selectedPlace != null) {
                    DetailsPanel(
                        place = uiState.selectedPlace,
                        onBackClick = { viewModel.onBackFromDetails() },
                        showBackButton = false,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    EmptyPanel(
                        message = "Выберите место для просмотра",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

// ─── Reusable panels ─────────────────────────────────────────────────────────

@Composable
private fun CategoriesPanel(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        item {
            Text(
                text = "Категории",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        items(categories) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category.id) }
            )
        }
    }
}

@Composable
private fun PlacesPanel(
    places: List<Place>,
    onPlaceClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onClick = { onPlaceClick(place.id) }
            )
        }
    }
}

@Composable
private fun DetailsPanel(
    place: Place,
    onBackClick: () -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(id = place.imageRes),
            contentDescription = place.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = place.fullDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            PanelInfoRow(
                icon = Icons.Default.LocationOn,
                label = "Адрес",
                value = place.address
            )
            if (place.workHours.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                PanelInfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Часы работы",
                    value = place.workHours
                )
            }
            if (place.phone.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                PanelInfoRow(
                    icon = Icons.Default.Phone,
                    label = "Телефон",
                    value = place.phone
                )
            }
            if (place.geoLink.isNotBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(place.geoLink))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Открыть на карте")
                }
            }
            if (showBackButton) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Text("← Назад к списку")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EmptyPanel(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
private fun PanelInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
