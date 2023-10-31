package com.codepath.demoproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavoritesAdapter(
    private val context: Context,
    private val animals: MutableList<DisplayAnimal>,
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = animals[position]
        holder.bind(animal)
    }

    override fun getItemCount() = animals.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val favAnimalImageView = itemView.findViewById<ImageView>(R.id.favAnimalImage)
        private val favAnimalNameTextView = itemView.findViewById<TextView>(R.id.favAnimalName)
        private val favSpeciesTextView = itemView.findViewById<TextView>(R.id.favAnimalSpecies)

        fun bind(animal: DisplayAnimal) {
            favAnimalNameTextView.text = animal.name
            favSpeciesTextView.text = animal.species

            if (animal.photo != "") {
                Glide.with(context).load(animal.photo).centerCrop().into(favAnimalImageView)
            } else {
                Glide.with(context).load(R.drawable.ic_launcher_foreground).centerCrop()
                    .into(favAnimalImageView)
            }

        }

    }

    companion object {
        const val LOG_TAG = "FavoritesAdapter"
    }
}