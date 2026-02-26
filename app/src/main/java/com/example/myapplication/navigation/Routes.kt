package com.example.myapplication.navigation

sealed class Routes(val route: String) {
    object Categories : Routes("categories")
    object Places : Routes("places/{categoryId}") {
        fun createRoute(categoryId: Int) = "places/$categoryId"
    }
    object Details : Routes("details/{placeId}") {
        fun createRoute(placeId: Int) = "details/$placeId"
    }
}
