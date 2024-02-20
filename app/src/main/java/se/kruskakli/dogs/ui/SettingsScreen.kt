package se.kruskakli.dogs.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import se.kruskakli.dogs.domain.BreedViewModel

@Composable
fun SettingsScreen(
    viewModel: BreedViewModel,
    navController: NavController
) {
    Text("Settings")
}