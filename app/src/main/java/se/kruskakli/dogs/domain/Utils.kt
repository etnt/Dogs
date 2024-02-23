package se.kruskakli.dogs.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    return EncryptedSharedPreferences.create(
        "encrypted_preferences",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun saveCounterValue(context: Context, counter: Int) {
    val prefs = getEncryptedSharedPreferences(context)
    prefs.edit().putInt("counter_key", counter).apply()
}

fun readCounterValue(context: Context): Int {
    val prefs = getEncryptedSharedPreferences(context)
    return prefs.getInt("counter_key", 0) // Default to 0 if not found
}
