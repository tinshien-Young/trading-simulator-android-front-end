package com.example.mytradingsimulatorlite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytradingsimulatorlite.data.repository.PortfolioRepository
import com.example.mytradingsimulatorlite.data.repository.StockRepository
import kotlinx.coroutines.launch

class BuySellViewModel : ViewModel() {
    private val stockRepository = StockRepository()
    
    var currentPrice by mutableStateOf(0.0)
        private set
    
    var quantity by mutableStateOf("")
    
    var message by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
        private set

    val balance: Double get() = PortfolioRepository.portfolio?.cashBalance ?: 0.0

    fun loadPrice(symbol: String) {
        viewModelScope.launch {
            isLoading = true
            val prices = stockRepository.getPrices()
            currentPrice = prices.find { it.symbol == symbol }?.price ?: 0.0
            isLoading = false
        }
    }

    fun executeOrder(symbol: String, type: String, onOrderExecuted: () -> Unit) {
        val q = quantity.toIntOrNull()
        if (q == null || q <= 0) {
            message = "Please enter a valid quantity"
            return
        }

        viewModelScope.launch {
            isLoading = true
            val error = PortfolioRepository.executeOrder(
                symbol = symbol,
                quantity = q,
                type = type
            )

            if (error != null) {
                message = error
            } else {
                message = "Order executed successfully!"
                onOrderExecuted()
            }
            isLoading = false
        }
    }
}
