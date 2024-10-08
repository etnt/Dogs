package se.kruskakli.dogs.db

import kotlinx.coroutines.flow.Flow
import se.kruskakli.dogs.domain.FavoriteBreed

class BreedRepository(private val breedDao: BreedDao) {

    val readAllFavorites: Flow<List<Favorites>> = breedDao.readAllFavorites()

    suspend fun fetchFavoriteFileNames() : List<String> = breedDao.fetchFavoriteFileNames()

    suspend fun fetchFavoriteBreeds() : List<FavoriteBreed> = breedDao.fetchFavoriteBreeds()

    fun readBreed(name: String): Flow<BreedInfo> = breedDao.readBreed(name)

    suspend fun insertFavorite(favorite: Favorites) {
        breedDao.insertFavorite(favorite)
    }
    suspend fun removeFavorite(id: String) {
        breedDao.removeFavorite(id)
    }

    suspend fun insertBreed(breed: BreedInfo) {
        breedDao.insertBreed(breed)
    }
}