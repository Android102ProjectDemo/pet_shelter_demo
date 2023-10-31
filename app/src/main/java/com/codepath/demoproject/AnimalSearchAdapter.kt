package com.codepath.demoproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AnimalSearchAdapter(
    private val context: Context,
    private val animals: MutableList<Animal>,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<AnimalSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = animals[position]
        holder.bind(animal)
    }

    override fun getItemCount() = animals.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val animalImageView = itemView.findViewById<ImageView>(R.id.animalImage)
        private val animalNameTextView = itemView.findViewById<TextView>(R.id.animalName)
        private val speciesTextView = itemView.findViewById<TextView>(R.id.animalSpecies)
        private val favButton = itemView.findViewById<Button>(R.id.favoritesButton)

        fun bind(animal: Animal) {
            animalNameTextView.text = animal.name
            speciesTextView.text = animal.species

            if (animal.mediaImageUrl != "") {
                Glide.with(context).load(animal.mediaImageUrl).centerCrop().into(animalImageView)
            } else {
                Glide.with(context).load(R.drawable.ic_launcher_foreground).centerCrop()
                    .into(animalImageView)
            }


            // Currently, you can keep adding duplicates. To prevent this,
            // you can add the ID from the API and prevent an insert on the duplicate ID
            favButton.setOnClickListener {
                val name = animal.name
                val species = animal.species
                val photo = animal.mediaImageUrl

                lifecycleOwner.lifecycleScope.launch(IO) {
                    (context.applicationContext as AnimalApplication).db.animalDao().insert(
                        AnimalEntity(
                            name = name, species = species, photo = photo
                        )
                    )
                }
                Toast.makeText(context, "Added $name to favorites.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        const val LOG_TAG = "AnimalSearchAdapter"
    }
}