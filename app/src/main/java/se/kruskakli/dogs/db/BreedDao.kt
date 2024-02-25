package se.kruskakli.dogs.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorites)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeFavorite(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBreed(breed: BreedInfo)

    @Query("SELECT * FROM favorites")
    fun readAllFavorites(): Flow<List<Favorites>>

    @Query("SELECT * FROM breeds WHERE name = :name")
    fun readBreed(name: String): Flow<BreedInfo>

}