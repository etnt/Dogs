package se.kruskakli.dogs.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.File
import java.util.Properties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.kruskakli.dogs.network.KtorClient

class BreedViewModel : ViewModel() {

    private val ktorClient = KtorClient(api_key = "live_EH6RpMtIb6ROsaKpBixAA1hBODjzZVm847Rxt8GYOweZ0DegyEhqfKveiMz6xsTv")

    private val _currentBreed = MutableStateFlow<BreedUi?>(null)
    val currentBreed: StateFlow<BreedUi?> = _currentBreed.asStateFlow()

    private val _navigateToSettings = MutableStateFlow(false)
    val navigateToSettings: StateFlow<Boolean> = _navigateToSettings.asStateFlow()

    fun resetBreed() {
        _currentBreed.value = null
    }

    fun fetchRandomBreed() {
        viewModelScope.launch(Dispatchers.IO) {
            ktorClient.getRandomBreed().onSuccess {
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
