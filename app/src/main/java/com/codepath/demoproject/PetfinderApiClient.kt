package com.codepath.demoproject

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class PetfinderApiClient(
    private val tokenListener: TokenListener,
    private val animalListener: AnimalsListener
) {
    private val client = OkHttpClient()
    fun getBearerToken() {

        val requestBody: FormBody = FormBody.Builder().add(GRANT_TYPE_KEY, GRANT_TYPE_VAL)
            .add(CLIENT_ID_KEY, BuildConfig.ID).add(CLIENT_SECRET_KEY, BuildConfig.SECRET).build()


        val request: Request = Request.Builder().url(TOKEN_URL).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.v("$LOG_TAG/onFailure", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    Log.v("$LOG_TAG/onResponse", responseData)
                }

                val responseJSONObject = responseData?.let { JSONObject(it) }
                val bearerToken = responseJSONObject?.getString(ACCESS_TOKEN_KEY).toString()
                // Edge case, no token received and token expires at 3600 minutes per API
                Log.v("$LOG_TAG/token", bearerToken)
                tokenListener.onTokenReceived(bearerToken)
            }
        })
    }

    fun getAllAnimals(bearerToken: String, zipCode: String) {

        Log.v(LOG_TAG, "$ANIMALS_URL?location=$zipCode")
        val request: Request = Request.Builder().url("$ANIMALS_URL?location=$zipCode").get()
            .addHeader(AUTH_HEADER, "$AUTH_BEARER $bearerToken").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val handler = Handler(Looper.getMainLooper())

                if (responseData != null) {
                    handler.post {

                        Log.v("$LOG_TAG/getAllAnimals", responseData)
                        val animalJsonObject = JSONObject(responseData)
                        val animalsArray: JSONArray = animalJsonObject.getJSONArray("animals")

                        val animals = mutableListOf<Animal>()
                        for (i in 0 until animalsArray.length()) {
                            val animalObject = animalsArray.getJSONObject(i)
                            val name = animalObject.getString("name")
                            val species = animalObject.getString("species")
                            val photosArray = animalObject.getJSONArray("photos")
                            val photos = mutableListOf<Photo>()

                            for (j in 0 until photosArray.length()) {
                                val photoObject = photosArray.getJSONObject(j)
                                val url = photoObject.getString("small")
                                val photo = Photo(url)
                                photos.add(photo)
                            }
                            val animal = Animal(name, species, photos)

                            animals.add(animal)
                        }


                        animalListener.onAnimalsReceived(animals)
                    }
                }
            }
        })

    }

    interface AnimalsListener {
        fun onAnimalsReceived(animals: MutableList<Animal>)
    }


    interface TokenListener {
        fun onTokenReceived(bearerToken: String)
    }

    companion object {
        const val GRANT_TYPE_KEY = "grant_type"
        const val GRANT_TYPE_VAL = "client_credentials"
        const val CLIENT_ID_KEY = "client_id"
        const val CLIENT_SECRET_KEY = "client_secret"
        const val TOKEN_URL = "https://api.petfinder.com/v2/oauth2/token"
        const val LOG_TAG = "PetfinderApiClient"
        const val ACCESS_TOKEN_KEY = "access_token"
        const val ANIMALS_URL = "https://api.petfinder.com/v2/animals"
        const val AUTH_HEADER = "Authorization"
        const val AUTH_BEARER = "Bearer"
    }
}