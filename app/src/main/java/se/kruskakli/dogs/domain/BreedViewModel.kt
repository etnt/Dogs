package se.kruskakli.dogs.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import se.kruskakli.dogs.data.Breed
import se.kruskakli.dogs.network.KtorClient

class BreedViewModel : ViewModel() {

    private val ktorClient = KtorClient(api_key = "live_EH6RpMtIb6ROsaKpBixAA1hBODjzZVm847Rxt8GYOweZ0DegyEhqfKveiMz6xsTv")

    private val _currentBreed = MutableStateFlow<BreedUi?>(null)
    val currentBreed: StateFlow<BreedUi?> = _currentBreed.asStateFlow()

    fun resetBreed() {
        _currentBreed.value = null
    }

    fun fetchRandomBreed() {
        viewModelScope.launch(Dispatchers.IO) {
            ktorClient.getRandomBreed().onSuccess {
                _currentBreed.value = it
            }.onFailure {
                _currentBreed.value = null
            }
        }
    }

    fun applySettings(settingsData: SettingsData) {
        // TODO: API key should be stored with Room.
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.ShowBreed -> {
                resetBreed()
                fetchRandomBreed()
            }

            is MainIntent.SaveSettings -> {
                applySettings(intent.settingsData)
            }
        }
    }
}