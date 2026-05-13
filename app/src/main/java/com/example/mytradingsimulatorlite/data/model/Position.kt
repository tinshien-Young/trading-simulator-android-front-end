package com.example.mytradingsimulatorlite.data.model

data class Position(
    val symbol: String,
    val quantity: Int,
    val averagePrice: Double
)