package se.kruskakli.dogs.data

import kotlinx.serialization.Serializable

@Serializable
data class Breed(
    val bred_for: String,
    val height: Height,
    val id: Int,
    val life_span: String,
    val name: String,
    val reference_image_id: String,
    val temperament: String,
    val weight: Weight,
    val breed_group: String? = null
)