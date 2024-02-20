package se.kruskakli.dogs.domain

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                BreedScreen(viewModel = viewModel, navController = navController)
            }
        }
        composable(route = Screen.SettingsScreen.route) {
            TopBar(viewModel, navController) {
                SettingsScreen(viewModel = viewModel, navController = navController)
            }
        }
    }
}

