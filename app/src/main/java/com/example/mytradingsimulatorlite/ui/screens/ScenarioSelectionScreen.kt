package com.example.mytradingsimulatorlite.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytradingsimulatorlite.data.model.Scenario
import com.example.mytradingsimulatorlite.viewmodel.ScenarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioSelectionScreen(
    onScenarioSelected: () -> Unit,
    vm: ScenarioViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.loadScenarios()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Scenario") })
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (vm.errorMessage != null) {
                Text(
                    text = vm.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (vm.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(vm.scenarios) { scenario ->
                        ScenarioItem(scenario = scenario, onClick = {
                            vm.selectScenario(scenario, onScenarioSelected)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun ScenarioItem(scenario: Scenario, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = scenario.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = scenario.description ?: "", style = MaterialTheme.typography.bodyMedium)
            if (scenario.startDate != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Starts: ${scenario.startDate}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
