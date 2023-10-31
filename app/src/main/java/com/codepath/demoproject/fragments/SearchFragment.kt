package com.codepath.demoproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.demoproject.Animal
import com.codepath.demoproject.AnimalSearchAdapter
import com.codepath.demoproject.MainActivity
import com.codepath.demoproject.PetfinderApiClient
import com.codepath.demoproject.R


class SearchFragment : Fragment(), PetfinderApiClient.TokenListener,
    PetfinderApiClient.AnimalsListener {
    private lateinit var petfinderApiClient: PetfinderApiClient
    private lateinit var bearerToken: String
    private lateinit var searchProgressBar: ContentLoadingProgressBar
    private lateinit var emptySearchText: TextView
    private lateinit var animalSearchRV: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        petfinderApiClient = PetfinderApiClient(this, this)
        petfinderApiClient.getBearerToken()
    }

    override fun onTokenReceived(bearerToken: String) {
        Log.d(MainActivity.LOG_TAG, "Received token: $bearerToken")
        this.bearerToken = bearerToken
    }

    override fun onAnimalsReceived(animals: List<Animal>) {
        val animalsToDisplay = animals.toMutableList()
        emptySearchText.visibility = INVISIBLE
        searchProgressBar.hide()
        animalSearchRV.adapter =
            view?.let { AnimalSearchAdapter(it.context, animalsToDisplay) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.animal_search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item =
            menu.findItem(R.id.action_animal_search).actionView as androidx.appcompat.widget.SearchView
        item.queryHint = "Enter a zip code..."
        item.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.v(LOG_TAG, query)
                if (::bearerToken.isInitialized) petfinderApiClient.getAllAnimals(
                    bearerToken,
                    query
                )
                else Log.d(LOG_TAG, "Bearer token has not been received yet.")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        activity?.title = "Search animals"
        searchProgressBar =
            view.findViewById<View>(R.id.animalSearchProgressBar) as ContentLoadingProgressBar
        searchProgressBar.hide()
        emptySearchText = view.findViewById<View>(R.id.emptySearch) as TextView
        animalSearchRV = view.findViewById<View>(R.id.animalSearchList) as RecyclerView
        val context = view.context
        animalSearchRV.layoutManager = LinearLayoutManager(context)
        animalSearchRV.addItemDecoration(DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL))
        return view
    }

    companion object {
        private const val LOG_TAG = "SearchFragment"

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}