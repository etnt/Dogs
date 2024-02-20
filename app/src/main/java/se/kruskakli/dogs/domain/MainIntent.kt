package se.kruskakli.dogs.domain


sealed class MainIntent {
    object ShowBreed : MainIntent()
    data class SaveSettings(val settingsData: SettingsData) : MainIntent()
}

data class SettingsData(
    val newApiKey: String
)
