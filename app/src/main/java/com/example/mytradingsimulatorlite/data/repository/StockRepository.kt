package com.example.mytradingsimulatorlite.data.repository

import com.example.mytradingsimulatorlite.data.model.PriceResponse
import com.example.mytradingsimulatorlite.data.model.Stock
import com.example.mytradingsimulatorlite.data.model.StockInfo
import com.example.mytradingsimulatorlite.data.network.RetrofitClient

class StockRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getStocks(): List<StockInfo> {
        return apiService.getStocks(PortfolioRepository.authHeader)
    }

    suspend fun getPrices(): List<PriceResponse> {
        return apiService.getPrices(PortfolioRepository.authHeader)
    }

    suspend fun getStockList(): List<Stock> {
        val info = getStocks().associateBy { it.symbol }
        val prices = getPrices()
        return prices.map { price ->
            Stock(
                symbol = price.symbol,
                name = info[price.symbol]?.name ?: price.symbol,
                price = price.price
            )
        }
    }
}
