package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.widget.Toast

class FavoritosActivity : AppCompatActivity() {
    private lateinit var db: FavoritosDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    private var lista = mutableListOf<Personaje>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        db = FavoritosDatabase.getInstance(this)
        recyclerView = findViewById(R.id.rvFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lista = db.obtenerTodos().toMutableList()

        adapter = CharacterAdapter(
            lista,
            favoritosIds = lista.map { it.id }.toMutableList(),
            onClick = { personaje ->
                // Ir al detail
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("characterId", personaje.id)
                startActivity(intent)
            },
            onFavoriteClick = { personaje ->
                // Al tocar el corazon en favoritos lo eliminamos
                if (db.esFavorito(personaje.id)) {
                    db.eliminarFavorito(personaje.id)
                    Toast.makeText(this, "${personaje.name} eliminado de favoritos", Toast.LENGTH_SHORT).show()
                    // refrescar lista local
                    lista.clear()
                    lista.addAll(db.obtenerTodos())
                    adapter.updateCharacters(lista)
                    adapter.updateFavoritosIds(lista.map { it.id })
                }
            }
        )

        recyclerView.adapter = adapter
    }
}
