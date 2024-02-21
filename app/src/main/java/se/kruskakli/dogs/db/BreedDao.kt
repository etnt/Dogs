package se.kruskakli.dogs.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorites)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBreed(breed: BreedInfo)

    @Query("SELECT * FROM favorites")
    fun readAllFavorites(): Flow<List<Favorites>>

    @Query("SELECT * FROM breeds WHERE name = :name")
    fun readBreed(name: String): Flow<BreedInfo>

}