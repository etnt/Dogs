package se.kruskakli.dogs.domain


sealed class MainIntent {
    object ShowBreed : MainIntent()
    object ShowFavorites : MainIntent()
    object MarkAsFavorite : MainIntent()
    object UnmarkAsFavorite : MainIntent()
    data class SaveSettings(val settingsData: SettingsData) : MainIntent()
}

data class SettingsData(
    val newApiKey: String
)
