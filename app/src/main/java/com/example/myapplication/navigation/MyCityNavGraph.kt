package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.MyCityViewModel
import com.example.myapplication.ui.screens.CategoriesScreen
import com.example.myapplication.ui.screens.DetailsScreen
import com.example.myapplication.ui.screens.PlacesScreen

@Composable
fun MyCityNavGraph(
    navController: NavHostController,
    viewModel: MyCityViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.Categories.route
    ) {
        composable(Routes.Categories.route) {
            CategoriesScreen(
                categories = uiState.categories,
                onCategoryClick = { categoryId ->
                    viewModel.onCategorySelected(categoryId)
                    navController.navigate(Routes.Places.createRoute(categoryId))
                }
            )
        }

        composable(
            route = Routes.Places.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: return@composable
            val category = uiState.categories.find { it.id == categoryId }
            PlacesScreen(
                categoryTitle = category?.title ?: "",
                places = uiState.placesBySelectedCategory,
                onPlaceClick = { placeId ->
                    viewModel.onPlaceSelected(placeId)
                    navController.navigate(Routes.Details.createRoute(placeId))
                },
                onBackClick = {
                    viewModel.onBackFromPlaces()
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.Details.route,
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) {
            val place = uiState.selectedPlace
            if (place != null) {
                DetailsScreen(
                    place = place,
                    onBackClick = {
                        viewModel.onBackFromDetails()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
