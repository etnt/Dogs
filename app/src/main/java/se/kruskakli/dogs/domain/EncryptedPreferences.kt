package se.kruskakli.dogs.domain

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveApiKey(apiKey: String) {
        sharedPreferences.edit().putString("api_key", apiKey).apply()
    }

    fun getApiKey(): String {
        return sharedPreferences.getString("api_key", "") ?: ""
    }

    fun saveCounterValue(value: Int) {
        sharedPreferences.edit().putInt("counter_value", value).apply()
    }

    fun readCounterValue(): Int {
        return sharedPreferences.getInt("counter_value", 10)
    }
}
