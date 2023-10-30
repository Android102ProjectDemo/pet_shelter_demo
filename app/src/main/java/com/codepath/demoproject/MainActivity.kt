package com.codepath.demoproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.demoproject.databinding.ActivityMainBinding
import com.codepath.demoproject.fragments.FavoritesFragment
import com.codepath.demoproject.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
        val searchFragment: Fragment = SearchFragment()
        val favoritesFragment: Fragment = FavoritesFragment()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)


        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.action_search -> fragment = searchFragment
                R.id.action_favorites -> fragment = favoritesFragment
            }
            fragmentManager.beginTransaction().replace(R.id.rlContainer, fragment).commit()
            true
        }
        bottomNavigationView.selectedItemId = R.id.action_search

    }


    companion object {
        const val LOG_TAG = "MainActivity"

    }
}