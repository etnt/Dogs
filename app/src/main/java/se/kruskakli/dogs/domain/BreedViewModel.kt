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
import se.kruskakli.dogs.db.BreedDao
import se.kruskakli.dogs.db.BreedInfo
import se.kruskakli.dogs.db.BreedRepository
import se.kruskakli.dogs.db.Favorites
import se.kruskakli.dogs.network.KtorClient
import se.kruskakli.dogs.domain.MainIntent
import se.kruskakli.dogs.domain.SettingsData
import se.kruskakli.dogs.domain.FavoriteImage

@HiltViewModel
class BreedViewModel @Inject constructor(
    private val dao: BreedDao,
    private val encryptedPreferences: EncryptedPreferences,
    private val imageDownloader: ImageDownloader,
    private val ktorClient: KtorClient
) : ViewModel() {

    private val breedRepository = BreedRepository(dao)

    private var _apiKey = MutableStateFlow(encryptedPreferences.getApiKey())
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _images = MutableStateFlow<List<FavoriteImage>>(emptyList())
    val images: StateFlow<List<FavoriteImage>> = _images.asStateFlow()

    private val _selectedImage = MutableStateFlow<FavoriteImage?>(null)
    val selectedImage: StateFlow<FavoriteImage?> = _selectedImage.asStateFlow()

    private val _favoriteCount = MutableStateFlow(0)
    val favoriteCount: StateFlow<Int> = _favoriteCount.asStateFlow()

    // Method to load images from the repository and update _images
    fun loadFavoriteImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteBreeds = breedRepository.fetchFavoriteBreeds()
            _images.value = emptyList()
            favoriteBreeds.forEach { (filename, breedName) ->
                imageDownloader.readImage(filename)?.let {
                    _images.value = _images.value + FavoriteImage(it, filename, breedName)
                }
            }
            _favoriteCount.value = _images.value.size
        }
    }

    // Method to select an image
    fun selectImage(image: FavoriteImage) {
        _selectedImage.value = image
    }

    // Method to clear the selected image
    fun clearSelectedImage() {
        _selectedImage.value = null
    }

    // Holds the current breed that is being displayed on the screen.
    private val _currentBreed = MutableStateFlow<BreedUi?>(null)
    val currentBreed: StateFlow<BreedUi?> = _currentBreed.asStateFlow()

    private val _navigateToSettings = MutableStateFlow(false)
    val navigateToSettings: StateFlow<Boolean> = _navigateToSettings.asStateFlow()

    fun addBreed(breed: BreedInfo) {
        viewModelScope.launch(Dispatchers.IO) { breedRepository.insertBreed(breed) }
    }

    fun addFavorite(favorite: Favorites) {
        viewModelScope.launch(Dispatchers.IO) {
            breedRepository.insertFavorite(favorite)
            _favoriteCount.value += 1
        }
    }

    fun deleteFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            breedRepository.removeFavorite(id)
            _favoriteCount.value -= 1
        }
    }

    fun getApiKey(): String = _apiKey.value

    fun setApiKey(newApiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            encryptedPreferences.saveApiKey(newApiKey)
            _apiKey.value = newApiKey
            ktorClient.updateApiKey(newApiKey)
        }
    }

    fun resetBreed() {
        _currentBreed.value = null
    }

    fun fetchRandomBreed() {
        viewModelScope.launch(Dispatchers.IO) {
            ktorClient
                .getRandomBreed()
                .onSuccess {
                    Log.d("BreedViewModel", "Fetched breed: $it")
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
        encryptedPreferences.saveApiKey(settingsData.newApiKey)
        _apiKey.value = settingsData.newApiKey
        ktorClient.updateApiKey(settingsData.newApiKey)
        resetNavigation()
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.ShowBreed -> {
                Log.d("BreedViewModel", "ShowBreed")
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
            is MainIntent.ShowSelectedImage -> {
                selectImage(intent.image)
            }
            is MainIntent.DeleteSelectedImage -> {
                val id = intent.image.filename.substringBefore(".jpeg")
                deleteFavorite(id)
                deleteImage(intent.image.filename)
                clearSelectedImage()
                loadFavoriteImages()
            }
            is MainIntent.ClearSelectedImage -> {
                clearSelectedImage()
            }
            is MainIntent.SaveSettings -> {
                applySettings(intent.settingsData)
            }
            is MainIntent.UpdateApiKey -> {
                setApiKey(intent.newApiKey)
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
