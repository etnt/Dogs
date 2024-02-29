package se.kruskakli.dogs.domain

import android.graphics.Bitmap


data class FavoriteImage(
    val bitmap: Bitmap,
    val filename: String,
    val breedName: String
)