package com.example.mytradingsimulatorlite.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytradingsimulatorlite.data.model.Stock
import com.example.mytradingsimulatorlite.viewmodel.StockListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    onBuyClick: (String) -> Unit,
    onSellClick: (String) -> Unit,
    onBackToScenarios: () -> Unit,
    vm: StockListViewModel = viewModel()
) {
    val stocks by vm.stocks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stock List") },
                navigationIcon = {
                    IconButton(onClick = onBackToScenarios) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Scenarios")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Portfolio Info & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Scenario: ${vm.scenarioCode}", style = MaterialTheme.typography.bodyMedium)
                    if (vm.isHistorical) {
                        Text(text = "Date: ${vm.currentDate}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
                
                if (vm.isHistorical) {
                    Row {
                        IconButton(onClick = { vm.resetScenario() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset")
                        }
                        Button(
                            onClick = { vm.advanceDay() },
                            enabled = vm.nextDayCooldown == 0
                        ) {
                            Text(if (vm.nextDayCooldown > 0) "${vm.nextDayCooldown}s" else "Next Day")
                        }
                    }
                }
            }

            if (vm.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = vm.errorMessage!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (vm.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(stocks) { stock ->
                        StockRow(
                            stock = stock,
                            onBuy = { onBuyClick(stock.symbol) },
                            onSell = { onSellClick(stock.symbol) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StockRow(stock: Stock, onBuy: () -> Unit, onSell: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stock.symbol, style = MaterialTheme.typography.titleLarge)
                Text(text = stock.name, style = MaterialTheme.typography.bodySmall)
            }
            
            Text(
                text = "$${String.format("%.2f", stock.price)}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Row {
                Button(
                    onClick = onBuy,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("BUY", style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = onSell,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("SELL", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
