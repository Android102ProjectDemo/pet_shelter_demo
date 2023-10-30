package com.codepath.demoproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AnimalSearchAdapter(
    private val context: Context, private val animals: MutableList<Animal>
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

        /*init {
            itemView.findViewById<Button>(R.id.saveExerciseButton).setOnClickListener {
                val name = itemView.findViewById<TextView>(R.id.exerciseName).text.toString()
                val description =
                    itemView.findViewById<TextView>(R.id.exerciseDescription).text.toString()
                val user = ParseUser.getCurrentUser()
                val bitmap = (mediaImageView.drawable as BitmapDrawable).bitmap
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                val file = ParseFile("image.png", byteArray)

                submitExercise(name, description, user, file)
            }
        }*/

        fun bind(animal: Animal) {
            animalNameTextView.text = animal.name
            speciesTextView.text = animal.species

            if (animal.mediaImageUrl != "") {
                Glide.with(context).load(animal.mediaImageUrl).centerCrop()
                    .into(animalImageView)
            } else {
                Glide.with(context).load(R.drawable.ic_launcher_foreground).centerCrop()
                    .into(animalImageView)
            }

        }
    }

    /*private fun submitExercise(
        name: String?, description: String, user: ParseUser, file: ParseFile
    ) {
        val exercise = Exercise()
        if (name != null) {
            exercise.setName(name)
        }
        exercise.setDescription(description)
        exercise.setUser(user)
        exercise.setImage(file)
        exercise.saveInBackground { e ->
            if (e != null) {
                Log.e(TAG, "Error while saving exercise.")
                e.printStackTrace()
                Toast.makeText(
                    context, "Error while saving exercise.", Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.i(TAG, "Successfully saved exercise.")
                Toast.makeText(
                    context, "Successfully saved exercise.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }*/

    companion object {
        const val TAG = "AnimalSearchAdapter/"
    }
}