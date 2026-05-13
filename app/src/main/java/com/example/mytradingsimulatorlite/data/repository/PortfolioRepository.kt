package com.example.mytradingsimulatorlite.data.repository

import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.mytradingsimulatorlite.data.model.*
import com.example.mytradingsimulatorlite.data.network.RetrofitClient

object PortfolioRepository {
    private val apiService = RetrofitClient.instance
    
    var authHeader by mutableStateOf("")
    var username by mutableStateOf("")
    var currentScenario by mutableStateOf<Scenario?>(null)
    
    var portfolio by mutableStateOf<PortfolioResponse?>(null)
    var orderHistory by mutableStateOf<List<OrderResponse>>(emptyList())

    fun setAuth(user: String, password: String) {
        username = user
        val auth = "$username:$password"
        authHeader = "Basic " + Base64.encodeToString(auth.toByteArray(), Base64.NO_WRAP)
    }

    fun logout() {
        authHeader = ""
        username = ""
        currentScenario = null
        portfolio = null
        orderHistory = emptyList()
    }

    suspend fun registerUser(username: String, password: String): Boolean {
        return try {
            apiService.register(AuthRequest(username, password))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getScenarios(): List<Scenario> {
        return apiService.getScenarios(authHeader)
    }

    suspend fun initializePortfolio(scenarioCode: String, balance: Double): Boolean {
        val response = apiService.initializePortfolio(authHeader, PortfolioInitializeRequest(scenarioCode, balance))
        portfolio = response
        return true
    }

    suspend fun refreshPortfolio() {
        portfolio = apiService.getPortfolio(authHeader)
        orderHistory = apiService.getOrderHistory(authHeader)
    }

    suspend fun advanceDay(): Boolean {
        apiService.advanceDay(authHeader)
        refreshPortfolio()
        return true
    }

    suspend fun resetScenario(): Boolean {
        portfolio = apiService.resetPortfolio(authHeader)
        refreshPortfolio()
        return true
    }

    suspend fun executeOrder(symbol: String, quantity: Int, type: String): String? {
        return try {
            val request = OrderRequest(symbol, quantity)
            if (type.lowercase() == "buy") {
                apiService.buyStock(authHeader, request)
            } else {
                apiService.sellStock(authHeader, request)
            }
            refreshPortfolio()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            e.message ?: "Order failed"
        }
    }
}
