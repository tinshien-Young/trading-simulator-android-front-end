package com.example.mytradingsimulatorlite.data.model

data class Scenario(
    val code: String,
    val name: String,
    val type: String, // LIVE or HISTORICAL
    val startDate: String?,
    val description: String?
)

data class PortfolioInitializeRequest(
    val scenarioCode: String,
    val startingBalance: Double
)

data class PortfolioResponse(
    val scenarioCode: String,
    val currentDate: String?,
    val startingBalance: Double,
    val cashBalance: Double,
    val holdingsValue: Double,
    val totalPortfolioValue: Double,
    val holdings: List<Holding>
)

data class Holding(
    val symbol: String,
    val name: String,
    val quantity: Int,
    val averageBuyPrice: Double,
    val currentPrice: Double,
    val marketValue: Double,
    val unrealizedProfitLoss: Double
)

data class OrderResponse(
    val orderId: Long,
    val symbol: String,
    val side: String, // BUY or SELL
    val quantity: Int,
    val pricePerShare: Double,
    val totalValue: Double,
    val scenarioMarketDate: String?,
    val executedAt: String
)

data class StockInfo(
    val symbol: String,
    val name: String,
    val currency: String
)

data class PriceResponse(
    val symbol: String,
    val price: Double,
    val marketDate: String?,
    val scenarioCode: String,
    val source: String
)

data class AdvanceResponse(
    val scenarioCode: String,
    val currentDate: String
)

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val username: String
)
