package com.example.mytradingsimulatorlite.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytradingsimulatorlite.viewmodel.PortfolioViewModel
import java.util.Locale


@Composable
fun PortfolioScreen(onLogout: () -> Unit, vm: PortfolioViewModel = viewModel()) {
    val portfolio = vm.portfolio
    val orders = vm.orders
    val username = vm.username

    LaunchedEffect(Unit) {
        vm.loadPortfolio()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Profile and Logout Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Placeholder Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = username, style = MaterialTheme.typography.titleLarge)
            }
            
            IconButton(onClick = {
                vm.logout()
                onLogout()
            }) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Portfolio", style = MaterialTheme.typography.headlineLarge)
        
        Spacer(Modifier.height(16.dp))

        if (portfolio != null) {
            // Upper part: Holding data and cash
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Value", style = MaterialTheme.typography.labelMedium)
                    val totalValueStr = String.format(Locale.US, "%.2f", portfolio.totalPortfolioValue)
                    Text("$$totalValueStr", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    
                    Spacer(Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Cash Balance", style = MaterialTheme.typography.labelSmall)
                            val balanceStr = String.format(Locale.US, "%.2f", portfolio.cashBalance)
                            Text("$$balanceStr", style = MaterialTheme.typography.bodyLarge)
                        }
                        Column {
                            Text("Holdings Value", style = MaterialTheme.typography.labelSmall)
                            val holdingsValueStr = String.format(Locale.US, "%.2f", portfolio.holdingsValue)
                            Text("$$holdingsValueStr", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Positions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(portfolio.holdings) { holding ->
                    val marketValueStr = String.format(Locale.US, "%.2f", holding.marketValue)
                    val avgPriceStr = String.format(Locale.US, "%.2f", holding.averageBuyPrice)
                    ListItem(
                        headlineContent = { Text(holding.symbol) },
                        supportingContent = { Text("${holding.quantity} shares @ $$avgPriceStr") },
                        trailingContent = { 
                            Column {
                                Text("$$marketValueStr", fontWeight = FontWeight.Bold)
                                val profitColor = if (holding.unrealizedProfitLoss >= 0) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
                                Text(
                                    text = String.format(Locale.US, "%+.2f", holding.unrealizedProfitLoss),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = profitColor
                                )
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            Box(modifier = Modifier.weight(1f)) {
                Text("Loading portfolio...")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Lower part: Order History
        Text("Order History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(orders) { order ->
                val priceStr = String.format(Locale.US, "%.2f", order.pricePerShare)
                ListItem(
                    headlineContent = { Text("${order.side} ${order.symbol}") },
                    supportingContent = { Text("${order.quantity} shares @ $$priceStr on ${order.scenarioMarketDate ?: "Live"}") },
                    trailingContent = { 
                        val color = if (order.side == "BUY") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        Text(order.side, color = color, fontWeight = FontWeight.Bold)
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
