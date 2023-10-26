package com.codepath.demoproject

import android.util.Log
import androidx.core.os.BuildCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PetfinderApiClient {
    private val client = OkHttpClient()

    fun getBearerToken() {
        val requestBody: FormBody =
            FormBody.Builder()
                .add(GRANT_TYPE_KEY, GRANT_TYPE_VAL)
                .add(CLIENT_ID_KEY, BuildConfig.ID)
                .add(CLIENT_SECRET_KEY, BuildConfig.SECRET)
                .build()


        val request: Request = Request.Builder()
            .url(TOKEN_URL)
            .post(requestBody)
            .build()

        Log.v("req", request.toString())

        Log.v("req", request.toString())

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.v("ERR", "no token")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    Log.v("response", responseData)
                }

                //val jsonObject = responseData?.let { JSONObject(it) }
                //val token = jsonObject?.getString("access_token")
                // Use the token as needed
                // You may also want to handle token expiration (if returned in the response)
            }
        })
    }

    companion object {
        const val GRANT_TYPE_KEY = "grant_type"
        const val GRANT_TYPE_VAL = "client_credentials"
        const val CLIENT_ID_KEY = "client_id"
        const val CLIENT_SECRET_KEY = "client_secret"
        val TOKEN_URL = "https://api.petfinder.com/v2/oauth2/token"
    }
}