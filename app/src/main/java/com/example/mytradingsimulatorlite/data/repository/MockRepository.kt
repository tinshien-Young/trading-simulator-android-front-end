package com.example.mytradingsimulatorlite.data.repository

import com.example.mytradingsimulatorlite.data.model.Portfolio
import com.example.mytradingsimulatorlite.data.model.Position
import com.example.mytradingsimulatorlite.data.model.Stock

import com.example.mytradingsimulatorlite.data.model.OrderHistory

//mock data is no longer useful

class MockRepository {
    fun getStocks() = listOf(
        Stock("AAPL", "Apple", 175.0),
        Stock("MSFT", "Microsoft", 380.0),
        Stock("AMZN", "Amazon", 185.0),
        Stock("GOOGL", "Google", 140.0),
        Stock("TSLA", "Tesla", 250.0)
    )

    fun getPortfolio() = Portfolio(
        balance = 10000.0,
        totalValue = 15000.0,
        positions = listOf(
            Position("AAPL", 10, 150.0),
            Position("TSLA", 5, 200.0)
        )
    )

    fun getOrderHistory() = listOf(
        OrderHistory("AAPL", 10, 150.0, "buy"),
        OrderHistory("TSLA", 5, 200.0, "buy"),
        OrderHistory("MSFT", 2, 350.0, "sell")
    )
}
