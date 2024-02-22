package se.kruskakli.dogs.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import se.kruskakli.dogs.db.BreedDao
import se.kruskakli.dogs.db.BreedDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BreedDatabase {
        return Room.databaseBuilder(
            context,
            BreedDatabase::class.java,
            "breed.db"
        ).build()
    }

    @Provides
    fun provideBreedDao(database: BreedDatabase): BreedDao {
        return database.breedDao
    }
}