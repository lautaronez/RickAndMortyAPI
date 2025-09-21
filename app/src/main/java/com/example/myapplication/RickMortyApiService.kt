package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RickMortyApiService {

    // Get character by id
    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int): Call<Personaje>

    // Get character by name
    @GET("character/")
    fun getCharacterByName(@retrofit2.http.Query("name") name: String): Call<PersonajesRickMorty>
}