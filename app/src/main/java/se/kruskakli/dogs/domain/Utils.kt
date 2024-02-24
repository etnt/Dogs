package se.kruskakli.dogs.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
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
