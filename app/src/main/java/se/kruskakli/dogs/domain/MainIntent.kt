package se.kruskakli.dogs.domain

import android.graphics.Bitmap


sealed class MainIntent {
    object ShowBreed : MainIntent()
    object ShowFavorites : MainIntent()
    object MarkAsFavorite : MainIntent()
    object UnmarkAsFavorite : MainIntent()
    data class ShowSelectedImage(val image: FavoriteImage) : MainIntent()
    data class DeleteSelectedImage(val image: FavoriteImage) : MainIntent()
    object ClearSelectedImage : MainIntent()

    data class SaveSettings(val settingsData: SettingsData) : MainIntent()
}

data class SettingsData(
    val newApiKey: String
)
