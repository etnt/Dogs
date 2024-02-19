package se.kruskakli.dogs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import se.kruskakli.dogs.data.Breeds
import se.kruskakli.dogs.data.BreedsItem
import se.kruskakli.dogs.network.KtorClient
import se.kruskakli.dogs.ui.theme.DogsTheme

class MainActivity : ComponentActivity() {

    private val ktorClient = KtorClient(api_key = "live_EH6RpMtIb6ROsaKpBixAA1hBODjzZVm847Rxt8GYOweZ0DegyEhqfKveiMz6xsTv")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var breeds by remember {
                mutableStateOf<BreedsItem?>(null)
            }

            LaunchedEffect(key1 = Unit, block = {
                ktorClient.getRandomBreed().onSuccess {
                    Log.d("MainActivity", "SUCCESS: $it")
                    breeds = it
                }.onFailure {
                    Log.d("MainActivity", "ERROR: $it")
                    breeds = null
                }
            })

            DogsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text("Breed: ${breeds?.let{it} ?: "Loading..."}")
                }
            }
        }
    }
}
