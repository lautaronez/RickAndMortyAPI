package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


// Adapter class to bind a list of characters to a RecyclerView
class CharacterAdapter(
    private val characters: List<Personaje>,
    private val onClick: (Personaje) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    // ViewHolder class to hold references to the item views
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCharacter: ImageView = view.findViewById(R.id.ivCharacter)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvSpecies: TextView = view.findViewById(R.id.tvSpecies)
    }

    // Inflate the item layout and create a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return ViewHolder(view)
    }

    // Return the total number of items in the list
    override fun getItemCount() = characters.size

    // Bind data from a character object to the item views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        // Set character name and species
        holder.tvName.text = character.name
        holder.tvSpecies.text = character.species

        // Load character image
        Glide.with(holder.itemView).load(character.image).into(holder.ivCharacter)

        //Listener clic
        holder.itemView.setOnClickListener { onClick(character) }
    }
}
