package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class PersonajesRickMorty(
    val results: List<Personaje> // List of characters returned by the API
)

// Data class representing a single character
data class Personaje(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("origin")
    val origin: Origen,
    @SerializedName("location")
    val location: Location
)

// Data class representing the origin of the character
data class Origen(
    val name: String,
    val url: String
)

// Data class representing the last known location of the character
data class Location(
    val name: String,
    val url: String
)