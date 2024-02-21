package se.kruskakli.dogs.domain

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import se.kruskakli.dogs.db.BreedDatabase
import se.kruskakli.dogs.network.KtorClient
import se.kruskakli.dogs.ui.theme.DogsTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            BreedDatabase::class.java,
            "breed.db"
        ).build()
    }
    private val viewModel by viewModels<BreedViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(BreedViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return BreedViewModel(db.breedDao) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val viewModelFactory = BreedViewModelFactory(application)
            //val viewModel: BreedViewModel = ViewModelProvider(this, viewModelFactory).get(BreedViewModel::class.java)
                
            viewModel.fetchRandomBreed()
            DogsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(viewModel)
                }
            }
        }
    }
}

