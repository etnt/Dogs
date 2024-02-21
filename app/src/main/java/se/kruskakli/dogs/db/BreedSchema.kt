package se.kruskakli.dogs.db

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "favorites")
data class Favorites(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val url: String,
    val name: String,
    val height: Int,
    val width: Int
)

@Entity(tableName = "breeds")
data class BreedInfo(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val bred_for: String,
    val breed_group: String?,
    val height: String,
    val life_span: String,
    val temperament: String,
    val weight: String
)