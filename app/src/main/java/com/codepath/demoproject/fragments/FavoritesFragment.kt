package com.codepath.demoproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.demoproject.AnimalApplication
import com.codepath.demoproject.DisplayAnimal
import com.codepath.demoproject.FavoritesAdapter
import com.codepath.demoproject.R
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    private lateinit var favRecyclerView: RecyclerView
    private var favorites: MutableList<DisplayAnimal> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_favorites, container, false
        )
        favRecyclerView = view.findViewById<View>(R.id.favAnimalSearchList) as RecyclerView

        val context = view.context
        favRecyclerView.layoutManager = LinearLayoutManager(context)
        favRecyclerView.adapter = view?.let { FavoritesAdapter(requireContext(), favorites) }


        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                lifecycleScope.launch(IO) {
                    (requireActivity().application as AnimalApplication).db.animalDao().getAll()
                        .collect { databaseList ->
                            databaseList.map { entity ->
                                DisplayAnimal(
                                    name = entity.name,
                                    species = entity.species,
                                    photo = entity.photo
                                )
                            }.also { mappedList ->
                                val favEntries = mappedList.map { favEntryEntity ->
                                    DisplayAnimal(
                                        favEntryEntity.name,
                                        favEntryEntity.species,
                                        favEntryEntity.photo,
                                    )
                                }
                                favorites.clear()
                                favorites.addAll(favEntries)
                                databaseList.forEach {
                                    Log.d(
                                        LOG_TAG, "Entry:${it.name}, ${it.species}, ${it.photo}"
                                    )
                                }
                                requireActivity().runOnUiThread {
                                    favRecyclerView.adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                }

            }
        })
        return view


    }

    companion object {

        const val LOG_TAG = "FavoritesFragment"
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}