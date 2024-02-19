package se.kruskakli.dogs.domain

import se.kruskakli.dogs.data.BreedsItem

data class BreedUi(
    val bred_for: String,
    val breed_group: String?,
    val height: String,
    val id: Int,
    val life_span: String,
    val name: String,
    val reference_image_id: String,
    val temperament: String,
    val weight: String,
    val image: Image
) {
    data class Image(
        val height: Int,
        val id: String,
        val url: String,
        val width: Int
    )
}

fun BreedsItem.toBreedUi(): BreedUi {
    val breed = breeds.first()
    return BreedUi(
        bred_for = breed.bred_for,
        breed_group = breed.breed_group,
        height = "${breed.height.metric} cm",
        id = breed.id,
        life_span = breed.life_span,
        name = breed.name,
        reference_image_id = breed.reference_image_id,
        temperament = breed.temperament,
        weight = "${breed.weight.metric} kg",
        image = BreedUi.Image(
            height = height,
            id = id,
            url = url,
            width = width
        )
    )
}