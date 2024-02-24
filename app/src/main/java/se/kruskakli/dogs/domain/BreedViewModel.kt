package se.kruskakli.dogs.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import se.kruskakli.dogs.BuildConfig
import se.kruskakli.dogs.db.BreedDao
import se.kruskakli.dogs.db.BreedDatabase
import se.kruskakli.dogs.db.BreedRepository
import se.kruskakli.dogs.db.BreedInfo
import se.kruskakli.dogs.db.Favorites
import se.kruskakli.dogs.network.KtorClient
import javax.inject.Inject


/*
   This code defines a ViewModel that has two dependencies: BreedDao and EncryptedPreferences.
   Hilt will automatically provide the necessary arguments (dao and encryptedPreferences)
   when creating an instance of BreedViewModel.
 */

@HiltViewModel
class BreedViewModel @Inject constructor(
    private val dao: BreedDao,
    private val encryptedPreferences: EncryptedPreferences
) : ViewModel() {

    private val breedRepository = BreedRepository(dao)

    fun addBreed(breed: BreedInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            breedRepository.insertBreed(breed)
        }
    }

    private val apiKey = BuildConfig.API_KEY
    private val ktorClient = KtorClient(api_key = apiKey)

    private val _currentBreed = MutableStateFlow<BreedUi?>(null)
    val currentBreed: StateFlow<BreedUi?> = _currentBreed.asStateFlow()

    private val _limitCounter = MutableStateFlow<Int>(encryptedPreferences.readCounterValue())
    val limitCounter: StateFlow<Int> = _limitCounter.asStateFlow()

    private val _navigateToSettings = MutableStateFlow(false)
    val navigateToSettings: StateFlow<Boolean> = _navigateToSettings.asStateFlow()

    fun resetBreed() {
        _currentBreed.value = null
    }

    fun fetchRandomBreed() {
        if (_limitCounter.value == 0) {
            _navigateToSettings.value = true
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            ktorClient.getRandomBreed().onSuccess {
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
            }.onFailure {
                _currentBreed.value = null
                _navigateToSettings.value = true
            }
        }
    }

    fun resetNavigation() {
        _navigateToSettings.value = false
    }

    fun applySettings(settingsData: SettingsData) {
        // TODO: API key should be stored with Room.
        resetNavigation()
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.ShowBreed -> {
                resetNavigation()
                resetBreed()
                fetchRandomBreed()
            }
            is MainIntent.SaveSettings -> {
                applySettings(intent.settingsData)
            }
        }
    }
}
