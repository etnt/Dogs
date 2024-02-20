package se.kruskakli.dogs.domain

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import se.kruskakli.dogs.data.Screen
import se.kruskakli.dogs.ui.BreedScreen
import se.kruskakli.dogs.ui.SettingsScreen
import se.kruskakli.dogs.ui.TopBar

@Composable
fun Navigation(
    viewModel: BreedViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.BreedScreen.route) {
        composable(route = Screen.BreedScreen.route) {
            TopBar(viewModel, navController) {
                BreedScreen(viewModel = viewModel)
            }
        }
        composable(route = Screen.SettingsScreen.route) {
            TopBar(viewModel, navController) {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}

