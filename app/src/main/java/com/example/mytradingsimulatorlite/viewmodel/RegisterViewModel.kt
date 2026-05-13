package com.example.mytradingsimulatorlite.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytradingsimulatorlite.data.repository.PortfolioRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var registerMessage by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isRegistered by mutableStateOf(false)
        private set

    fun onRegisterClick() {
        if (username.isBlank() || password.isBlank()) {
            registerMessage = "Please enter username and password!"
            return
        }
        if (password != confirmPassword) {
            registerMessage = "Passwords do not match!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            val success = PortfolioRepository.registerUser(username, password)
            if (success) {
                registerMessage = "Registration successful! You can now log in."
                isRegistered = true
            } else {
                registerMessage = "Registration failed. Username might already exist. Password should be more than 8 digits."
            }
            isLoading = false
        }
    }
}
