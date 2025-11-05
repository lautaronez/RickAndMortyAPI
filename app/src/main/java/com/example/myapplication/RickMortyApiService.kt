package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApiService {

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: Int): Call<Personaje>

    @GET("character/")
    fun getCharacterByName(@Query("name") name: String): Call<PersonajesRickMorty>
}
