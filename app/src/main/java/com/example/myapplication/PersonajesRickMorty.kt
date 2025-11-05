package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class PersonajesRickMorty(
    val results: List<Personaje>
)

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

data class Origen(
    val name: String,
    val url: String
)

data class Location(
    val name: String,
    val url: String
)
