package se.kruskakli.dogs.data

import kotlinx.serialization.Serializable

@Serializable
data class Height(
    val imperial: String,
    val metric: String
)