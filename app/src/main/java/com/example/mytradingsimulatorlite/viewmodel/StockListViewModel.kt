package com.example.mytradingsimulatorlite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytradingsimulatorlite.data.model.PortfolioResponse
import com.example.mytradingsimulatorlite.data.model.Stock
import com.example.mytradingsimulatorlite.data.repository.PortfolioRepository
import com.example.mytradingsimulatorlite.data.repository.StockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class StockListViewModel : ViewModel() {
    private val repository = StockRepository()

    private val _stocks = MutableStateFlow<List<Stock>>(emptyList())
    val stocks: StateFlow<List<Stock>> = _stocks

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var nextDayCooldown by mutableStateOf(0)
        private set

    val portfolio: PortfolioResponse? get() = PortfolioRepository.portfolio
    
    val scenarioCode: String get() = portfolio?.scenarioCode ?: ""
    val currentDate: String? get() = portfolio?.currentDate
    val isHistorical: Boolean get() = currentDate != null

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                PortfolioRepository.refreshPortfolio()
                _stocks.value = repository.getStockList()
            } catch (e: Exception) {
                handleError(e)
            }
            isLoading = false
        }
    }

    private fun handleError(e: Exception) {
        e.printStackTrace()
        if (e is HttpException) {
            try {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null) {
                    val json = JSONObject(errorBody)
                    val message = json.optString("message")
                    if (message.contains("API credits", ignoreCase = true)) {
                        errorMessage = "Too many requests! Please wait a minute for the API limits to reset."
                        return
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        errorMessage = "Failed to load market data. Please check your connection."
    }

    fun advanceDay() {
        if (nextDayCooldown > 0) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val success = PortfolioRepository.advanceDay()
                if (success) {
                    _stocks.value = repository.getStockList()
                    startCooldown()
                }
            } catch (e: Exception) {
                handleError(e)
            }
            isLoading = false
        }
    }

    private fun startCooldown() {
        viewModelScope.launch {
            nextDayCooldown = 60
            while (nextDayCooldown > 0) {
                delay(1000)
                nextDayCooldown--
            }
        }
    }

    fun resetScenario() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val success = PortfolioRepository.resetScenario()
                if (success) {
                    _stocks.value = repository.getStockList()
                }
            } catch (e: Exception) {
                handleError(e)
            }
            isLoading = false
        }
    }
}
