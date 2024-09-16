package se.kruskakli.dogs.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.kruskakli.dogs.R
import se.kruskakli.dogs.domain.BreedViewModel
import se.kruskakli.dogs.domain.MainIntent
import se.kruskakli.dogs.domain.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: BreedViewModel,
    navigationController: NavController,
    bodyContent: @Composable () -> Unit
) {
    val favoriteCounter = viewModel.favoriteCount.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopAppBar(
            title = {
                IconButton(onClick = {
                    viewModel.handleIntent(MainIntent.ShowFavorites)
                    navigationController.navigate(Screen.FavoriteScreen.route)
                }) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_dog_breeds),
                        contentDescription = "Favorites"
                    )
                }
            },
            actions = {

                IconButton(
                    onClick = {
                        Log.d("TopBar", "ShowBreed")
                        viewModel.handleIntent(MainIntent.ShowBreed)
                        navigationController.navigate(Screen.BreedScreen.route)
                    },
                    modifier = Modifier.padding(end = 12.dp)
                ){
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                }
                Text(
                    text = "${favoriteCounter.value}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(end = 12.dp)
                )
                IconButton(onClick = {
                    navigationController.navigate(Screen.SettingsScreen.route)
                }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        )
        Divider()
        bodyContent()
    }
}

@Composable
fun Divider() {
    Spacer(modifier = Modifier
        .height(1.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    )
}