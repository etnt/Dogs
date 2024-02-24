package se.kruskakli.dogs.domain

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EncryptedPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getEncryptedSharedPreferences(): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            "encrypted_preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveCounterValue(counter: Int) {
        val prefs = getEncryptedSharedPreferences()
        prefs.edit().putInt("counter_key", counter).apply()
    }

    fun readCounterValue(): Int {
        val prefs = getEncryptedSharedPreferences()
        return prefs.getInt("counter_key", 10) // Default to 10 if not found
    }
}



suspend fun downloadAndStoreImage(
        context: Context,
        imageUrl: String,
        filename: String
) {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false) // Important for accessing the bitmap
        .build()

    val result = imageLoader.execute(request)

    if (result is SuccessResult) {
        val bitmap = (result.drawable as BitmapDrawable).bitmap
        storeImage(context, bitmap, filename)
    }
}

fun storeImage(context: Context, bitmap: Bitmap, filename: String) {
    context.openFileOutput(filename, Context.MODE_PRIVATE).use { fos ->
        // Use JPEG format for storage
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    }
}
