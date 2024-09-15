package se.kruskakli.dogs.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import se.kruskakli.dogs.data.Breeds
import se.kruskakli.dogs.data.BreedsItem
import se.kruskakli.dogs.domain.BreedUi
import se.kruskakli.dogs.domain.toBreedUi

class KtorClient(private var api_key: String) {
    private var client = createHttpClient()

    private fun createHttpClient() = HttpClient(OkHttp) {
        defaultRequest { 
            url("https://api.thedogapi.com/v1/images/search?has_breeds=1")
            header("x-api-key", api_key)
        }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    fun updateApiKey(newApiKey: String) {
        api_key = newApiKey
        client.close()
        client = createHttpClient()
    }

    private var breedCache = mutableMapOf<String, BreedUi>()

    suspend fun getRandomBreed(): ApiOperation<BreedUi> {
        return safeApiCall {
            val response: List<BreedsItem> = client.get("").body()
            val breed = response.first().toBreedUi()
            breedCache[breed.name] = breed
            breed
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            ApiOperation.Failure(exception = e)
        }
    }
}

sealed interface ApiOperation<T> {
    data class Success<T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val exception: Exception) : ApiOperation<T>

    fun <R> mapSuccess(transform: (T) -> R): ApiOperation<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(exception)
        }
    }

    fun onSuccess(block: (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(exception)
        return this
    }
}
