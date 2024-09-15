package se.kruskakli.dogs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import se.kruskakli.dogs.domain.EncryptedPreferences
import se.kruskakli.dogs.network.KtorClient
import javax.inject.Provider

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideKtorClient(encryptedPreferences: Provider<EncryptedPreferences>): Provider<KtorClient> =
        Provider { KtorClient(api_key = encryptedPreferences.get().getApiKey()) }
}
