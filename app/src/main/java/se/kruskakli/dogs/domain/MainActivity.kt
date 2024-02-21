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

    /*
       This code uses the Room.databaseBuilder method to create a Room database instance.
       The databaseBuilder method takes three parameters: the application context,
       the database class (BreedDatabase::class.java), and the name of the database ("breed.db").
       The build method is then called to build the database. The by lazy keyword ensures
       that the database is only created when it's first accessed, and the same instance
       is returned for any subsequent accesses.
    */
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            BreedDatabase::class.java,
            "breed.db"
        ).build()
    }
    /*
       This code uses the viewModels delegate to create a BreedViewModel instance.
       The viewModels delegate takes a factoryProducer parameter, which is a lambda
       that returns a ViewModelProvider.Factory. This factory is used to create the
       BreedViewModel instance.

       The create method in the factory checks if modelClass is BreedViewModel or a
       subclass of BreedViewModel. If it is, it creates a BreedViewModel instance
       with db.breedDao as a parameter and returns it. If modelClass is not BreedViewModel
       or a subclass of BreedViewModel, it throws an IllegalArgumentException.

       The @Suppress("UNCHECKED_CAST") annotation is used to suppress the unchecked
       cast warning. This is safe because the code checks if modelClass is BreedViewModel
       or a subclass of BreedViewModel before the cast.
    */
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

