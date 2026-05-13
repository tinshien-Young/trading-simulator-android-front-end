package com.example.mytradingsimulatorlite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytradingsimulatorlite.data.repository.PortfolioRepository
import com.example.mytradingsimulatorlite.data.model.PortfolioResponse
import com.example.mytradingsimulatorlite.data.model.OrderResponse
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    val portfolio: PortfolioResponse? get() = PortfolioRepository.portfolio
    val orders: List<OrderResponse> get() = PortfolioRepository.orderHistory
    val username: String get() = PortfolioRepository.username

    fun loadPortfolio() {
        viewModelScope.launch {
            try {
                PortfolioRepository.refreshPortfolio()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        PortfolioRepository.logout()
    }
}
