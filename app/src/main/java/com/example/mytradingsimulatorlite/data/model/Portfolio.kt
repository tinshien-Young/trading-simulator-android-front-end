package com.example.mytradingsimulatorlite.data.model


data class Portfolio(
    val balance: Double,
    val totalValue: Double,
    val positions: List<Position>
)

data class PortfolioRequest(
    val scenarioCode: String,    // "LIVE", "COVID", "FINANCIAL_CRISIS"
    val startingBalance: Double
)


