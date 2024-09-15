package se.kruskakli.dogs.domain

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedPreferences @Inject constructor(@ApplicationContext private val context: Context) {
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
        return prefs.getInt("counter_key", 1000)
    }
}

class ImageDownloader @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun downloadAndStoreImage(imageUrl: String, filename: String) {
        val imageLoader = ImageLoader(context)
        val request =
                ImageRequest.Builder(context)
                        .data(imageUrl)
                        .allowHardware(false) // Important for accessing the bitmap
                        .build()

        val result = imageLoader.execute(request)

        if (result is SuccessResult) {
            val bitmap = (result.drawable as BitmapDrawable).bitmap
            storeImage(bitmap, filename)
        }
    }

    private fun storeImage(bitmap: Bitmap, filename: String) {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { fos ->
            // Use JPEG format for storage
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }
    }

    fun removeImage(filename: String) {
        context.deleteFile(filename)
    }

    fun readImage(filename: String): Bitmap? {
        logFilesInInternalStorage()
        
        //val file = File(context.filesDir, filename)
        //if (!file.exists()) {
        //    Log.d("ImageDownloader", "File does not exist: $filename")
        //    return null
        //}

        return try {
            context.openFileInput(filename).use { 
                BitmapFactory.decodeStream(it)
            }
        } catch (e: FileNotFoundException) {
            Log.d("ImageDownloader", "File not found: $filename , ${e.message}")
            null
        }
    }

    fun logFilesInInternalStorage() {
        val directory = context.filesDir
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                Log.d("InternalStorage", "File: ${file.name}")
            }
        } else {
            Log.d("InternalStorage", "No files found")
        }
    }
}
