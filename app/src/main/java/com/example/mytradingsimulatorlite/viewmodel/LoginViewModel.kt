package com.example.mytradingsimulatorlite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytradingsimulatorlite.data.repository.PortfolioRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    // 状态：输入框内容
    // Status: Input box content
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    // 状态：登录状态反馈
    // Status: Login status feedback
    var loginMessage by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    var isLoggedIn by mutableStateOf(false)
        private set

    fun onLoginClick() {
        if (username.isBlank() || password.isBlank()) {
            loginMessage = "Please enter username or password!"
        } else {
            viewModelScope.launch {
                isLoading = true
                PortfolioRepository.setAuth(username, password)
                try {
                    // Try to fetch scenarios to verify credentials
                    // refreshPortfolio() might fail if the user hasn't selected a scenario yet
                    PortfolioRepository.getScenarios()
                    loginMessage = "Success!"
                    isLoggedIn = true
                } catch (e: Exception) {
                    loginMessage = "Invalid username or password!"
                    PortfolioRepository.authHeader = "" // Clear invalid auth
                }
                isLoading = false
            }
        }
    }
}
