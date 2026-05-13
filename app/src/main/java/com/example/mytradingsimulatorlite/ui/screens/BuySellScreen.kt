package com.example.mytradingsimulatorlite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytradingsimulatorlite.viewmodel.BuySellViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuySellScreen(
    symbol: String,
    type: String, // "buy" or "sell"
    onBack: () -> Unit,
    onOrderExecuted: () -> Unit,
    vm: BuySellViewModel = viewModel()
) {
    LaunchedEffect(symbol) {
        vm.loadPrice(symbol)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${type.uppercase()} $symbol") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Market Price: $${vm.currentPrice}", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Available Balance: $${String.format("%.2f", vm.balance)}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = vm.quantity,
                onValueChange = { vm.quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (vm.message.isNotEmpty()) {
                Text(
                    text = vm.message,
                    color = if (vm.message.contains("success", ignoreCase = true)) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (vm.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { vm.executeOrder(symbol, type, onOrderExecuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == "buy") Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                ) {
                    Text("${type.uppercase()} $symbol")
                }
            }
        }
    }
}
