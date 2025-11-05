package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CharacterAdapter(
    private val characters: MutableList<Personaje>,
    private var favoritosIds: MutableList<Int> = mutableListOf(),
    private val onClick: (Personaje) -> Unit,
    private val onFavoriteClick: (Personaje) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCharacter: ImageView = view.findViewById(R.id.ivCharacter)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvSpecies: TextView = view.findViewById(R.id.tvSpecies)
        val ivFavorite: ImageView = view.findViewById(R.id.ivFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c = characters[position]
        holder.tvName.text = c.name
        holder.tvSpecies.text = c.species
        Glide.with(holder.itemView.context).load(c.image).into(holder.ivCharacter)

        // Cambia el ícono según si es favorito
        val esFav = favoritosIds.contains(c.id)
        holder.ivFavorite.setImageResource(
            if (esFav) R.drawable.fav_lleno else R.drawable.fav
        )

        holder.itemView.setOnClickListener { onClick(c) }
        holder.ivFavorite.setOnClickListener { onFavoriteClick(c) }
    }

    // --- helpers para actualizar datos sin recrear adapter ---
    fun updateCharacters(newList: List<Personaje>) {
        characters.clear()
        characters.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateFavoritosIds(newFavs: List<Int>) {
        favoritosIds.clear()
        favoritosIds.addAll(newFavs)
        notifyDataSetChanged()
    }
}

