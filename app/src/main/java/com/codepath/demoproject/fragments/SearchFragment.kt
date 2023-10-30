package com.codepath.demoproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.demoproject.R

import okhttp3.Headers
import org.json.JSONException
import org.json.JSONObject


class SearchFragment : Fragment() {
    private lateinit var searchProgressBar: ContentLoadingProgressBar
    private lateinit var emptySearchText: TextView
    private lateinit var animalSearchRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.animal_search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item =
            menu.findItem(R.id.action_animal_search).actionView as androidx.appcompat.widget.SearchView
        item.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
               // updateAdapter(query)
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
        //activity?.title = getString(R.string.action_bar_search)
        searchProgressBar =
            view.findViewById<View>(R.id.animalSearchProgressBar) as ContentLoadingProgressBar
        searchProgressBar.hide()
        emptySearchText = view.findViewById<View>(R.id.emptySearch) as TextView
        animalSearchRV = view.findViewById<View>(R.id.animaleSearchList) as RecyclerView
        val context = view.context
        animalSearchRV.layoutManager = LinearLayoutManager(context)
        return view
    }

    /*private fun updateAdapter(
        searchQuery: String
    ) {
        val exercisesToDisplay = mutableListOf<Pair<ExerciseWger, String>>()
        emptySearchText.visibility = INVISIBLE
        searchExercisesByName(searchQuery,
            onSuccess = { exercises ->
                if (exercises.isNotEmpty()) {
                    val maxExercises =
                        if (exercises.size > SEARCH_LIMIT) SEARCH_LIMIT else exercises.size
                    for (i in 0 until maxExercises) {
                        val exercise = getExerciseCompleteInfo(exercises[i],
                            onSuccess = { completeExercise ->
                                exercisesToDisplay.addAll(listOf(completeExercise))
                                exercisesSearchRV.adapter =
                                    view?.let { ExerciseAdapter(it.context, exercisesToDisplay) }
                                searchProgressBar.hide()

                            },
                            onFailure = { errorMessage ->
                                Log.d(TAG, "\nExercise in inner on failure: $errorMessage\n")
                                searchProgressBar.hide()

                            })
                        Log.d(TAG, "\nExercise: $exercise\n")
                    }
                } else {
                    Toast.makeText(context, "No exercises found.", Toast.LENGTH_SHORT).show()
                    searchProgressBar.hide()
                }
            },
            onFailure = { errorMessage ->
                Log.d(TAG, "\nExercise: $errorMessage\n")
                searchProgressBar.hide()

            }
        )

    }*/

    /*private fun searchExercisesByName(
        name: String, onSuccess: (List<ExerciseWger>) -> Unit, onFailure: (String) -> Unit
    ) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        val exercises = mutableListOf<ExerciseWger>()

        params[TERM_PARAM] = name
        params[SEARCH_LIMIT_PARAM] = SEARCH_LIMIT.toString()
        searchProgressBar.show()

        client[EXERCISE_SEARCH_URL, params, object : JsonHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int, headers: Headers, json: JSON
            ) {
                try {
                    val parsedJson = createJson().decodeFromString(
                        ExerciseSearchResponse.serializer(), json.jsonObject.toString()
                    )
                    parsedJson.suggestions?.let { list ->
                        exercises.addAll(list.mapNotNull { it.data })
                        onSuccess(exercises)
                    }
                } catch (e: JSONException) {
                    onFailure("Exception: $e")
                }
                Log.d(TAG, "searchExercisesByName -- response successful")
                searchProgressBar.hide()
            }

            override fun onFailure(
                statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?
            ) {
                searchProgressBar.hide()
                onFailure("Error retrieving from $EXERCISE_SEARCH_URL")
                Log.d(TAG, "search by name -- Error retrieving from $EXERCISE_SEARCH_URL")
            }
        }]
    }*/

    /*private fun getExerciseCompleteInfo(
        exercise: ExerciseWger,
        onSuccess: (Pair<ExerciseWger, String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params[SEARCH_LIMIT_PARAM] = SEARCH_LIMIT.toString()

        client["$EXERCISE_DESCRIPTION_SEARCH_URL${exercise.id}/", params,
            object :
                JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int, headers: Headers, json: JSON
                ) {
                    searchProgressBar.hide()
                    val resultsJSON: JSONObject = json.jsonObject as JSONObject
                    val exerciseRawDesc = resultsJSON.get("description")
                    val doc = Jsoup.parse(exerciseRawDesc as String)
                    var exerciseDesc = doc.select("p").text()
                    if (exerciseDesc.isEmpty()) {
                        exerciseDesc = "No description found."
                    }
                    //Log.d(TAG, exerciseDesc)
                    val completeExercise: Pair<ExerciseWger, String> =
                        Pair(exercise, exerciseDesc)
                    Log.d(TAG, "getExercise info -- response successful")
                    onSuccess(completeExercise)
                }

                override fun onFailure(
                    statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?
                ) {
                    searchProgressBar.hide()
                    onFailure("Error retrieving from $EXERCISE_SEARCH_URL")
                    Log.d(TAG, "search by name -- Error retrieving from $EXERCISE_SEARCH_URL")
                }
            }]
    }*/

    companion object {
        private const val TAG = "ExerciseFragment/"
        private const val EXERCISE_SEARCH_URL = "https://wger.de/api/v2/exercise/search/"
        private const val EXERCISE_DESCRIPTION_SEARCH_URL = "https://wger.de/api/v2/exercise/"
        private const val SEARCH_LIMIT = 3
        private const val TERM_PARAM = "term"
        private const val SEARCH_LIMIT_PARAM = "search_limit"

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}