package com.example.mytradingsimulatorlite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytradingsimulatorlite.data.model.Scenario
import com.example.mytradingsimulatorlite.data.repository.PortfolioRepository
import kotlinx.coroutines.launch

class ScenarioViewModel : ViewModel() {
    var scenarios by mutableStateOf<List<Scenario>>(emptyList())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadScenarios() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                scenarios = PortfolioRepository.getScenarios()
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Failed to load scenarios."
            }
            isLoading = false
        }
    }

    fun selectScenario(scenario: Scenario, onSelected: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val success = PortfolioRepository.initializePortfolio(scenario.code, 1000000.0)
                if (success) {
                    PortfolioRepository.currentScenario = scenario
                    onSelected()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Failed to initialize portfolio. You might have reached API limits."
            }
            isLoading = false
        }
    }
}
