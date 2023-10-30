package com.codepath.demoproject

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AnimalsReponse(
    @SerialName("animals")
    val animals: List<Animal>?
)
@Keep
@Serializable
data class Animal(
    @SerialName("name")
    val name: String? = null,
    @SerialName("species")
    val species: String?,
    @SerialName("photos")
    val photos: List<Photo>?,
) : java.io.Serializable {
    val mediaImageUrl = photos?.firstOrNull { it.url != null }?.url ?: ""
}

@Keep
@Serializable
data class Photo(
    @SerialName("small")
    val url: String?
) : java.io.Serializable