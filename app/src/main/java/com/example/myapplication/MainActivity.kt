package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: ImageButton
    private lateinit var btnFavoritos: ImageButton
    private lateinit var tvNoResults: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    private lateinit var favoritosDB: FavoritosDatabase

    private var personajes = mutableListOf<Personaje>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
        btnFavoritos = findViewById(R.id.btnFavoritos)
        tvNoResults = findViewById(R.id.tvNoResults)
        recyclerView = findViewById(R.id.rvPersonajes)

        recyclerView.layoutManager = LinearLayoutManager(this)

        favoritosDB = FavoritosDatabase.getInstance(this)

        // Inicializar adapter una sola vez
        adapter = CharacterAdapter(
            personajes,
            favoritosIds = favoritosDB.obtenerTodos().map { it.id }.toMutableList(),
            onClick = { personaje ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("characterId", personaje.id)
                startActivity(intent)
            },
            onFavoriteClick = { personaje ->
                toggleFavorito(personaje)
            }
        )

        recyclerView.adapter = adapter

        btnSearch.setOnClickListener { buscarPersonaje() }

        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
    }

    private fun toggleFavorito(personaje: Personaje) {
        if (favoritosDB.esFavorito(personaje.id)) {
            favoritosDB.eliminarFavorito(personaje.id)
            Toast.makeText(this, "${personaje.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
        } else {
            favoritosDB.agregarFavorito(personaje)
            Toast.makeText(this, "${personaje.name} agregado a favoritos", Toast.LENGTH_SHORT).show()
        }
        // Actualizar los ids de favoritos en el adapter
        val favIds = favoritosDB.obtenerTodos().map { it.id }
        adapter.updateFavoritosIds(favIds)
    }

    private fun buscarPersonaje() {
        val nombre = etSearch.text.toString().trim()
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.apiService.getCharacterByName(nombre)
            .enqueue(object : Callback<PersonajesRickMorty> {
                override fun onResponse(
                    call: Call<PersonajesRickMorty>,
                    response: Response<PersonajesRickMorty>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val results = response.body()!!.results
                        personajes.clear()
                        personajes.addAll(results)
                        adapter.updateCharacters(personajes)

                        // Actualizar favoritos en caso de que cambien
                        val favIds = favoritosDB.obtenerTodos().map { it.id }
                        adapter.updateFavoritosIds(favIds)

                        tvNoResults.visibility = if (personajes.isEmpty()) TextView.VISIBLE else TextView.GONE
                    } else {
                        personajes.clear()
                        adapter.updateCharacters(personajes)
                        tvNoResults.visibility = TextView.VISIBLE
                    }
                }

                override fun onFailure(call: Call<PersonajesRickMorty>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error de conexión: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
