package com.codepath.demoproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), PetfinderApiClient.TokenListener {
    private lateinit var petfinderApiClient: PetfinderApiClient
    private lateinit var bearerToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        petfinderApiClient = PetfinderApiClient(this)
        petfinderApiClient.getBearerToken()
    }

    override fun onTokenReceived(bearerToken: String) {
        Log.d(LOG_TAG, "Received token: $bearerToken")
        this.bearerToken = bearerToken
        petfinderApiClient.getAllAnimals(bearerToken)

    }

    companion object {
        const val LOG_TAG = "MainActivity"
    }
}