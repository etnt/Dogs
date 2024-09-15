package se.kruskakli.dogs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.kruskakli.dogs.domain.BreedViewModel
import se.kruskakli.dogs.domain.MainIntent
import se.kruskakli.dogs.domain.SettingsData

@Composable
fun SettingsScreen(
    viewModel: BreedViewModel,
    navController: NavController
) {
    var apiKey by remember { mutableStateOf(viewModel.getApiKey()) }
    var requestLimit by remember { mutableStateOf(viewModel.getLimitCounter().toString()) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("API Key") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = requestLimit,
            onValueChange = { requestLimit = it },
            label = { Text("Request Limit") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.handleIntent(
                    MainIntent.SaveSettings(
                        SettingsData(
                            newApiKey = apiKey,
                            newRequestLimit = requestLimit.toIntOrNull() ?: 0
                        )
                    )
                )
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
