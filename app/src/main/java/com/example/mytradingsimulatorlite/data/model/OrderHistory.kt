package com.example.mytradingsimulatorlite.data.model

data class OrderHistory(
    val symbol: String,
    val quantity: Int,
    val price: Double,
    val type: String    // "buy" or "sell"
)

data class OrderRequest(
    val symbol: String,
    val quantity: Int
)

data class Order(
    val symbol: String,
    val quantity: Int,
    val price: Double,
    val type: String    // "buy" or "sell"
)
