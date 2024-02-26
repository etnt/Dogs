package se.kruskakli.dogs.db

import kotlinx.coroutines.flow.Flow

class BreedRepository(private val breedDao: BreedDao) {

    val readAllFavorites: Flow<List<Favorites>> = breedDao.readAllFavorites()

    suspend fun fetchFavoriteFileNames() : List<String> = breedDao.fetchFavoriteFileNames()

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