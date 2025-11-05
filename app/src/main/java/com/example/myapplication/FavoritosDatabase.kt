package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavoritosDatabase(context: Context) : SQLiteOpenHelper(
    context,
    "favoritos.db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val crearTabla = """
            CREATE TABLE favoritos(
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                species TEXT,
                image TEXT
            )
        """.trimIndent()
        db?.execSQL(crearTabla)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS favoritos")
        onCreate(db)
    }

    // --- CRUD ---
    fun agregarFavorito(personaje: Personaje): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("id", personaje.id)
            put("name", personaje.name)
            put("species", personaje.species)
            put("image", personaje.image)
        }
        val resultado = db.insertWithOnConflict("favoritos", null, valores, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return resultado != -1L
    }

    fun eliminarFavorito(id: Int): Boolean {
        val db = writableDatabase
        val filasAfectadas = db.delete("favoritos", "id = ?", arrayOf(id.toString()))
        db.close()
        return filasAfectadas > 0
    }

    fun obtenerTodos(): List<Personaje> {
        val lista = mutableListOf<Personaje>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, species, image FROM favoritos ORDER BY name ASC", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val species = cursor.getString(2)
                val image = cursor.getString(3)

                lista.add(
                    Personaje(
                        id = id,
                        name = name,
                        status = "",
                        species = species ?: "",
                        type = "",
                        gender = "",
                        image = image ?: "",
                        origin = Origen("", ""),
                        location = Location("", "")
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun esFavorito(id: Int): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM favoritos WHERE id = ?", arrayOf(id.toString()))
        var existe = false
        if (cursor.moveToFirst()) {
            existe = cursor.getInt(0) > 0
        }
        cursor.close()
        db.close()
        return existe
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoritosDatabase? = null

        fun getInstance(context: Context): FavoritosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = FavoritosDatabase(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
