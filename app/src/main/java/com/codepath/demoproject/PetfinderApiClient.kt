package com.codepath.demoproject

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PetfinderApiClient(private val listener: TokenListener) {
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
                listener.onTokenReceived(bearerToken)
            }
        })
    }

    fun getAllAnimals(bearerToken: String) {
        val request: Request = Request.Builder().url(ANIMALS_URL).get()
            .addHeader(AUTH_HEADER, "$AUTH_BEARER $bearerToken").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure here
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                // Handle the response data here
                if (responseData != null) {
                    Log.v("$LOG_TAG/getAllAnimals", responseData)
                }
            }
        })

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