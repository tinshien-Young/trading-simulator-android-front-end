package com.example.mytradingsimulatorlite.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mytradingsimulatorlite.ui.screens.*

sealed class Screen(val route: String, val label: String) {
    object Login : Screen("login", "Login")
    object Registration : Screen("registration", "Registration")
    object ScenarioSelection : Screen("scenario_selection", "Scenarios")
    object Portfolio : Screen("portfolio", "Portfolio")
    object Stocks : Screen("stocks", "Stocks")
    object BuySell : Screen("buy_sell/{symbol}/{type}", "Buy/Sell") {
        fun createRoute(symbol: String, type: String) = "buy_sell/$symbol/$type"
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route != Screen.Login.route && 
                        currentDestination?.route != Screen.Registration.route &&
                        currentDestination?.route != Screen.ScenarioSelection.route &&
                        currentDestination?.route?.startsWith("buy_sell") != true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val items = listOf(Screen.Stocks, Screen.Portfolio)
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                if (screen == Screen.Stocks) Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                                else Icon(Icons.Filled.Person, contentDescription = null)
                            },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.ScenarioSelection.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Screen.Registration.route)
                    }
                )
            }

            composable(Screen.Registration.route) {
                RegistrationScreen(
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.ScenarioSelection.route) {
                ScenarioSelectionScreen(onScenarioSelected = {
                    navController.navigate(Screen.Stocks.route) {
                        popUpTo(Screen.ScenarioSelection.route) { inclusive = true }
                    }
                })
            }

            composable(Screen.Portfolio.route) {
                PortfolioScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Stocks.route) {
                StockListScreen(
                    onBuyClick = { symbol ->
                        navController.navigate(Screen.BuySell.createRoute(symbol, "buy"))
                    },
                    onSellClick = { symbol ->
                        navController.navigate(Screen.BuySell.createRoute(symbol, "sell"))
                    },
                    onBackToScenarios = {
                        navController.navigate(Screen.ScenarioSelection.route) {
                            popUpTo(Screen.Stocks.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.BuySell.route) { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
                val type = backStackEntry.arguments?.getString("type") ?: "buy"
                BuySellScreen(
                    symbol = symbol,
                    type = type,
                    onBack = { navController.popBackStack() },
                    onOrderExecuted = {
                        navController.navigate(Screen.Portfolio.route) {
                            popUpTo(Screen.Stocks.route)
                        }
                    }
                )
            }
        }
    }
}
