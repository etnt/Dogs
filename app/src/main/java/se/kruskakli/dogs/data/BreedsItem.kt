package se.kruskakli.dogs.data

import kotlinx.serialization.Serializable

@Serializable
data class BreedsItem(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)