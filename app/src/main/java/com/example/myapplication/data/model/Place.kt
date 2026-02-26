package com.example.myapplication.data.model

import androidx.annotation.DrawableRes

data class Place(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val shortDescription: String,
    val fullDescription: String,
    val address: String,
    val workHours: String,
    val phone: String = "",
    val geoLink: String = "",
    @DrawableRes val imageRes: Int
)
