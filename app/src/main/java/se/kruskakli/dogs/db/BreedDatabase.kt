package se.kruskakli.dogs.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorites::class, BreedInfo::class], version = 1, exportSchema = false)
abstract class BreedDatabase: RoomDatabase() {

    abstract val breedDao: BreedDao

    companion object {
        const val DATABASE_NAME = "breed_database"

        @Volatile
        private var INSTANCE: BreedDatabase? = null

        fun getDatabase(context: Context): BreedDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BreedDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}