package se.kruskakli.dogs.domain

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import se.kruskakli.dogs.BuildConfig
import se.kruskakli.dogs.db.BreedDao
import se.kruskakli.dogs.db.BreedInfo
import se.kruskakli.dogs.db.BreedRepository
import se.kruskakli.dogs.db.Favorites
import se.kruskakli.dogs.network.KtorClient

/*
  This code defines a ViewModel that has two dependencies: BreedDao and EncryptedPreferences.
  Hilt will automatically provide the necessary arguments (dao and encryptedPreferences)
  when creating an instance of BreedViewModel.
*/

@HiltViewModel
class BreedViewModel
@Inject
constructor(
        private val dao: BreedDao,
        private val encryptedPreferences: EncryptedPreferences,
        private val imageDownloader: ImageDownloader
) : ViewModel() {

    private val breedRepository = BreedRepository(dao)

    private val apiKey = BuildConfig.API_KEY
    private val ktorClient = KtorClient(api_key = apiKey)

    private val _images = MutableStateFlow<List<Bitmap>>(emptyList())
    val images: StateFlow<List<Bitmap>> = _images.asStateFlow()

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage.asStateFlow()

    // Method to load images from the repository and update _images
    fun loadFavoriteImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteFileNames = breedRepository.fetchFavoriteFileNames()
            Log.d("BreedViewModel", "Favorite file names: $favoriteFileNames")
            _images.value = emptyList()
            favoriteFileNames.forEach { filename ->
                imageDownloader.readImage(filename)?.let {
                    Log.d("BreedViewModel", "Favorite file names: ${it.width} x ${it.height}")
                    _images.value = _images.value + it
                }
            }   
        }
    }

    // Method to select an image
    fun selectImage(image: Bitmap) {
        _selectedImage.value = image
    }

    // Method to clear the selected image
    fun clearSelectedImage() {
        _selectedImage.value = null
    }

    // Holds the current breed that is being displayed on the screen.
    private val _currentBreed = MutableStateFlow<BreedUi?>(null)
    val currentBreed: StateFlow<BreedUi?> = _currentBreed.asStateFlow()

    // Holds the number of outstanding requests that remains.
    private val _limitCounter = MutableStateFlow<Int>(encryptedPreferences.readCounterValue())
    val limitCounter: StateFlow<Int> = _limitCounter.asStateFlow()

    // FIXME: Indicates if the app should navigate to the payment screen.
    private val _navigateToSettings = MutableStateFlow(false)
    val navigateToSettings: StateFlow<Boolean> = _navigateToSettings.asStateFlow()

    // Holds the list of favorite image filenames
    //private val _favoriteFileNames = MutableStateFlow<List<String>>(emptyList())
    //val favoriteFileNames: StateFlow<List<String>> = _favoriteFileNames.asStateFlow()

    fun addBreed(breed: BreedInfo) {
        viewModelScope.launch(Dispatchers.IO) { breedRepository.insertBreed(breed) }
    }

    fun addFavorite(favorite: Favorites) {
        viewModelScope.launch(Dispatchers.IO) { breedRepository.insertFavorite(favorite) }
    }

    fun deleteFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) { breedRepository.removeFavorite(id) }
    }



    fun resetBreed() {
        _currentBreed.value = null
    }

    fun fetchRandomBreed() {
        if (_limitCounter.value == 0) {
            _navigateToSettings.value = true
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            ktorClient
                    .getRandomBreed()
                    .onSuccess {
                        _limitCounter.value -= 1
                        encryptedPreferences.saveCounterValue(_limitCounter.value)
                        addBreed(
                                BreedInfo(
                                        name = it.name,
                                        bred_for = it.bred_for,
                                        breed_group = it.bred_for,
                                        height = it.height,
                                        life_span = it.life_span,
                                        temperament = it.temperament,
                                        weight = it.weight
                                )
                        )
                        _currentBreed.value = it
                    }
                    .onFailure {
                        _currentBreed.value = null
                        _navigateToSettings.value = true
                    }
        }
    }

    fun resetNavigation() {
        _navigateToSettings.value = false
    }

    fun applySettings(settingsData: SettingsData) {
        resetNavigation()
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.ShowBreed -> {
                resetNavigation()
                resetBreed()
                fetchRandomBreed()
            }
            is MainIntent.ShowFavorites -> {
                loadFavoriteImages()
            }
            is MainIntent.MarkAsFavorite -> {
                downloadImage(
                        _currentBreed.value!!.image.url,
                        "${_currentBreed.value!!.image.id}.jpeg"
                )
                addFavorite(
                        Favorites(
                                id = _currentBreed.value!!.image.id,
                                url = _currentBreed.value!!.image.url,
                                name = _currentBreed.value!!.name,
                                height = _currentBreed.value!!.image.height,
                                width = _currentBreed.value!!.image.width
                        )
                )
            }
            is MainIntent.UnmarkAsFavorite -> {
                deleteFavorite(_currentBreed.value!!.image.id)
                deleteImage("${_currentBreed.value!!.image.id}.jpeg")
            }
            is MainIntent.SaveSettings -> {
                applySettings(intent.settingsData)
            }
        }
    }

    fun getImage(filename: String) = imageDownloader.readImage(filename)

    private fun downloadImage(imageUrl: String, filename: String) {
        Log.d("BreedViewModel", "Downloading image: $imageUrl , $filename")
        viewModelScope.launch(Dispatchers.IO) {
            imageDownloader.downloadAndStoreImage(imageUrl, filename)
        }
    }

    private fun deleteImage(filename: String) {
        viewModelScope.launch(Dispatchers.IO) {
            imageDownloader.removeImage(filename)
        }
    }
}
